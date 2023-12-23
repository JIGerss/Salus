package team.glhf.salus.service.Impl;

import cn.hutool.core.thread.ThreadUtil;
import org.springframework.stereotype.Service;
import team.glhf.salus.annotation.AutoOperateGame;
import team.glhf.salus.dto.game.ConfigReq;
import team.glhf.salus.dto.game.SelectReq;
import team.glhf.salus.dto.game.StartGameReq;
import team.glhf.salus.entity.User;
import team.glhf.salus.enumeration.GameMessageType;
import team.glhf.salus.enumeration.GameRoleEnum;
import team.glhf.salus.enumeration.GameStageEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.GameException;
import team.glhf.salus.service.GameService;
import team.glhf.salus.service.PlaceService;
import team.glhf.salus.service.UserService;
import team.glhf.salus.vo.game.*;
import team.glhf.salus.websocket.Game;
import team.glhf.salus.websocket.GameEndPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steveny
 * @since 2023/12/11
 */
@Service
public class GameServiceImpl implements GameService {
    private final UserService userService;
    private final PlaceService placeService;
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public GameServiceImpl(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @Override
    public void joinOrCreateGame(GameEndPoint gameEndPoint) {
        String key = gameEndPoint.getKey();
        Game game;
        if (hasGame(key)) {
            game = getGameByKey(key);
            game.addPlayer(gameEndPoint);
        } else {
            game = new Game(this);
            game.setKey(key);
            game.addPlayer(gameEndPoint);
            game.setGameStageEnum(GameStageEnum.PREPARE);
            games.put(key, game);
        }
        sendGameInfo(game);
    }

    @Override
    @AutoOperateGame
    public void quitGame(GameEndPoint gameEndPoint, Game game) {
        game.removePlayer(gameEndPoint);
        if (game.getPlayers().isEmpty()) {
            games.remove(game.getKey());
        }
    }

    @Override
    @AutoOperateGame
    public void handleMessage(GameEndPoint gameEndPoint, String message, Game game) {
        if (!message.matches("^[0-9]{1,4}.?[0-9]{0,6},[0-9]{1,4}.?[0-9]{0,6}$")) {
            throw new GameException(HttpCodeEnum.DATA_PART_ILLEGAL);
        }
        double[] pos = Arrays.stream(message.split(",")).mapToDouble(Double::parseDouble).toArray();
        switch (game.getGameStageEnum()) {
            case PREPARE -> {
                gameEndPoint.setLongitude(pos[0]);
                gameEndPoint.setLatitude(pos[1]);
            }
            case LOADING -> {
                gameEndPoint.setLongitude(pos[0]);
                gameEndPoint.setLatitude(pos[1]);
                // 检查其他人是否已经发送了位置
                boolean flag = true;
                for (GameEndPoint player : game.getPlayers()) {
                    if (null == player.getLatitude() || null == player.getLongitude()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    game.setGameStageEnum(GameStageEnum.PLAYING);
                    ThreadUtil.execute(game);
                }
            }
            case PLAYING -> {
                if (GameRoleEnum.CAUGHT.equals(gameEndPoint.getRole())) {
                    throw new GameException(HttpCodeEnum.GAME_CAUGHT_MOUSE);
                }
                double distance = placeService.countDistance(gameEndPoint.getLongitude(),
                        gameEndPoint.getLatitude(), pos[0], pos[1]);
                gameEndPoint.setScore(gameEndPoint.getScore() + (int) (distance * 1000));
                gameEndPoint.setLongitude(pos[0]);
                gameEndPoint.setLatitude(pos[1]);
            }
            default -> throw new GameException(HttpCodeEnum.GAME_STAGE_ERROR);
        }
    }

    @Override
    @AutoOperateGame
    public void configureGame(ConfigReq configReq, Game game) {
        if (game.isStarted()) {
            throw new GameException(HttpCodeEnum.GAME_ALREADY_STARTED);
        }
        if (!game.isHost(configReq.getUserId())) {
            throw new GameException(HttpCodeEnum.GAME_PERMISSION_ERROR);
        }
        game.setTime(configReq.getTime());
    }

    @Override
    @AutoOperateGame
    public void selectRole(SelectReq selectReq, Game game) {
        if (game.isStarted()) {
            throw new GameException(HttpCodeEnum.GAME_ALREADY_STARTED);
        }
        for (GameEndPoint player : game.getPlayers()) {
            if (selectReq.getUserId().equals(player.getUserId())) {
                GameRoleEnum role = GameRoleEnum.getRoleByCode(selectReq.getRole());
                player.setRole(role);
                return;
            }
        }
        throw new GameException(HttpCodeEnum.USER_NOT_JOIN_GAME);
    }

    @Override
    @AutoOperateGame
    public void startGame(StartGameReq startGameReq, Game game) {
        if (game.isStarted()) {
            throw new GameException(HttpCodeEnum.GAME_ALREADY_STARTED);
        }
        if (!game.isHost(startGameReq.getUserId())) {
            throw new GameException(HttpCodeEnum.GAME_PERMISSION_ERROR);
        }
        boolean cat = false;
        boolean mouse = false;
        for (GameEndPoint player : game.getPlayers()) {
            if (GameRoleEnum.NULL.equals(player.getRole()) || 0 == game.getTime()) {
                throw new GameException(HttpCodeEnum.GAME_CONFIGURATION_ERROR);
            }
            cat = cat || GameRoleEnum.CAT.equals(player.getRole());
            mouse = mouse || GameRoleEnum.MOUSE.equals(player.getRole());
        }
        if (!(cat && mouse)) {
            throw new GameException(HttpCodeEnum.GAME_NO_BOTH_ROLES);
        }
        game.setGameStageEnum(GameStageEnum.LOADING);
    }

    @Override
    public Game getGameByKey(String key) {
        if (!hasGame(key)) {
            throw new GameException(HttpCodeEnum.GAME_NOT_EXIST);
        }
        return games.get(key);
    }

    @Override
    public void checkGameStatus(Game game) {
        for (GameEndPoint cat : game.getPlayers()) {
            if (GameRoleEnum.CAT.equals(cat.getRole())) {
                boolean noMouse = true;
                for (GameEndPoint mouse : game.getPlayers()) {
                    if (GameRoleEnum.MOUSE.equals(mouse.getRole())) {
                        double distance = placeService.countDistance(cat.getLongitude(), cat.getLatitude(),
                                mouse.getLongitude(), mouse.getLatitude()
                        );
                        // 已抓到
                        if (distance <= 0.005) {
                            String catName = userService.getUserById(cat.getUserId()).getNickname();
                            String mouseName = userService.getUserById(mouse.getUserId()).getNickname();
                            for (GameEndPoint player : game.getPlayers()) {
                                player.sendMessage(GameMessageVo.builder()
                                        .message(catName + " has caught " + mouseName + "!")
                                        .time(game.getTime())
                                        .messageType(GameMessageType.MESSAGE)
                                        .build()
                                );
                            }
                            cat.setCaught(cat.getCaught() + 1);
                            mouse.setRole(GameRoleEnum.CAUGHT);
                            sendGameInfo(game);
                        } else {
                            noMouse = false;
                        }
                    }
                }
                // 所有老鼠全部抓完，游戏结束
                if (noMouse) {
                    gameOver(game, false);
                    break;
                }
            }
        }
    }

    @Override
    public void gameOver(Game game, boolean mouseWin) {
        game.setGameStageEnum(GameStageEnum.OVER);
        game.setFlag(mouseWin ? GameRoleEnum.MOUSE.getCode() : GameRoleEnum.CAT.getCode());
        sendGameInfo(game);
    }

    @Override
    public void sendGameInfo(Game game) {
        switch (game.getGameStageEnum()) {
            case PREPARE, LOADING, PLAYING -> {
                List<GameNormalUserVo> players = new ArrayList<>();
                for (GameEndPoint player : game.getPlayers()) {
                    User user = userService.getUserById(player.getUserId());
                    GameNormalUserVo gameNormalUserVo = GameNormalUserVo.builder()
                            .userId(user.getId())
                            .nickname(user.getNickname())
                            .avatar(user.getAvatar())
                            .isHost(game.isHost(player.getUserId()))
                            .longitude(player.getLongitude())
                            .latitude(player.getLatitude())
                            .role(player.getRole())
                            .build();
                    players.add(gameNormalUserVo);
                }
                for (GameEndPoint player : game.getPlayers()) {
                    GameNormalInfoVo gameInfo = GameNormalInfoVo.builder()
                            .key(game.getKey())
                            .time(game.getTime())
                            .messageType(GameMessageType.NORMAL)
                            .gameStage(game.getGameStageEnum())
                            .players(players)
                            .userId(player.getUserId())
                            .build();
                    player.sendMessage(gameInfo);
                }
            }
            case OVER -> {
                List<GameOverUserVo> players = new ArrayList<>();
                for (GameEndPoint player : game.getPlayers()) {
                    User user = userService.getUserById(player.getUserId());
                    GameOverUserVo gameOverUserVo = GameOverUserVo.builder()
                            .userId(user.getId())
                            .nickname(user.getNickname())
                            .avatar(user.getAvatar())
                            .caught(player.getCaught())
                            .score(player.getScore())
                            .isHost(game.isHost(player.getUserId()))
                            .role(player.getRole())
                            .build();
                    players.add(gameOverUserVo);
                }
                for (GameEndPoint player : game.getPlayers()) {
                    GameOverInfoVo gameInfo = GameOverInfoVo.builder()
                            .winner(game.getFlag())
                            .key(game.getKey())
                            .messageType(GameMessageType.OVER)
                            .time(game.getTime())
                            .gameStage(game.getGameStageEnum())
                            .players(players)
                            .userId(player.getUserId())
                            .build();
                    player.sendMessage(gameInfo);
                }
            }
            default -> throw new GameException(HttpCodeEnum.GAME_NOT_EXIST);
        }
    }

    @Override
    public boolean hasGame(String key) {
        return games.containsKey(key);
    }
}
