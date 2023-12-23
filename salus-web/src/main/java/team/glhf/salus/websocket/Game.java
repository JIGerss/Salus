package team.glhf.salus.websocket;

import lombok.Getter;
import lombok.Setter;
import team.glhf.salus.enumeration.GameStageEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.GameException;
import team.glhf.salus.service.GameService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Steveny
 * @since 2023/12/11
 */
@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "BusyWait"})
@Getter
@Setter
public class Game implements Runnable {
    private int flag;
    private String key;
    private Integer time;
    private GameStageEnum gameStageEnum;
    private List<GameEndPoint> players;
    private final GameService gameService;

    public Game(GameService gameService) {
        this.gameStageEnum = GameStageEnum.INIT;
        this.players = new CopyOnWriteArrayList<>();
        this.gameService = gameService;
        this.time = 0;
        this.flag = 0;
    }

    @Override
    public void run() {
        while (time > 0 && flag == 0) {
            try {
                for (int i = 0; i < 5 && flag == 0; i++) {
                    Thread.sleep(200);
                    gameService.checkGameStatus(this);
                }
            } catch (InterruptedException e) {
                throw new GameException(HttpCodeEnum.SERVER_ERROR);
            } finally {
                time -= 1;
            }
        }
        if (flag == 0) {
            gameService.gameOver(this, true);
        }
    }

    public void addPlayer(GameEndPoint gameEndPoint) {
        players.add(gameEndPoint);
    }

    public void removePlayer(GameEndPoint gameEndPoint) {
        players.remove(gameEndPoint);
    }

    public boolean isStarted() {
        return !GameStageEnum.PREPARE.equals(gameStageEnum);
    }

    public boolean isHost(String userId) {
        return userId.equals(players.get(0).getUserId());
    }
}
