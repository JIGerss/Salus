package team.glhf.salus.vo.game;

import lombok.Getter;
import lombok.Setter;
import team.glhf.salus.enumeration.GameMessageType;
import team.glhf.salus.enumeration.GameStageEnum;

/**
 * @author Steveny
 * @since 2023/12/11
 */
@Getter
@Setter
public abstract class AbstractGameInfo {

    private String key;

    private GameMessageType messageType;

    private GameStageEnum gameStage;
}
