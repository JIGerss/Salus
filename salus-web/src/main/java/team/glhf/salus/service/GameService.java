package team.glhf.salus.service;

import team.glhf.salus.dto.game.ConfigReq;
import team.glhf.salus.dto.game.SelectReq;
import team.glhf.salus.dto.game.StartGameReq;
import team.glhf.salus.websocket.Game;
import team.glhf.salus.websocket.GameEndPoint;

/**
 * @author Steveny
 * @since 2023/12/12
 */
public interface GameService {
    void joinOrCreateGame(GameEndPoint gameEndPoint);

    void quitGame(GameEndPoint gameEndPoint, Game game);

    void handleMessage(GameEndPoint gameEndPoint, String message, Game game);

    void configureGame(ConfigReq configReq, Game game);

    void selectRole(SelectReq selectReq, Game game);

    void startGame(StartGameReq startGameReq, Game game);

    Game getGameByKey(String key);

    void checkGameStatus(Game game);

    void gameOver(Game game, boolean mouseWin);

    void sendGameInfo(Game game);

    boolean hasGame(String key);
}
