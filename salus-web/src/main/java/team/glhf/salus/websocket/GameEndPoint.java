package team.glhf.salus.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.glhf.salus.enumeration.GameRoleEnum;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.GameException;
import team.glhf.salus.result.Result;
import team.glhf.salus.service.GameService;
import team.glhf.salus.utils.JwtUtil;

import java.util.List;
import java.util.Map;

/**
 * GameEndPoint for websocket
 *
 * @author Steveny
 * @since 2023/12/9
 */
@Getter
@Setter
@Component
@NoArgsConstructor
@ServerEndpoint("/game/entry")
@SuppressWarnings("unused")
public class GameEndPoint {
    private String userId;
    private String key;
    private Session session;
    private GameRoleEnum role;
    private Double longitude;
    private Double latitude;
    private int score;
    private int caught;
    private static GameService gameService;

    @Autowired
    public void addGameService(GameService gameService) {
        GameEndPoint.gameService = gameService;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        if (null != parameterMap) {
            List<String> tokens = parameterMap.get("Authorization");
            List<String> keys = parameterMap.get("key");
            if (null == tokens || null == keys) {
                throw new GameException(HttpCodeEnum.TOKEN_REQUIRE);
            }
            String token = tokens.get(0);
            key = keys.get(0);
            role = GameRoleEnum.MOUSE;
            userId = JwtUtil.verifyAndGetUserId(token);
            gameService.joinOrCreateGame(this);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        gameService.handleMessage(this, message, null);
    }

    @OnClose
    public void onClose(Session session) {
        gameService.quitGame(this, null);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        for (HttpCodeEnum value : HttpCodeEnum.values()) {
            if (value.getMessage().equals(throwable.getMessage())) {
                sendMessage(Result.errorResult(value));
            }
        }
    }

    public void sendMessage(Object message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String text = objectMapper
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(Result.okResult(message));
            if (null != session) {
                session.getBasicRemote().sendText(text);
            }
        } catch (Exception e) {
            throw new GameException(HttpCodeEnum.SEND_MESSAGE_ERROR);
        }
    }
}
