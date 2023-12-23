package team.glhf.salus.vo.game;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import team.glhf.salus.enumeration.GameMessageType;
import team.glhf.salus.enumeration.GameStageEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Felix
 * @since 2023/11/09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class GameNormalInfoVo extends AbstractGameInfo implements Serializable {

    private String key;

    private String userId;

    private Integer time;

    private GameMessageType messageType;

    private GameStageEnum gameStage;

    private List<GameNormalUserVo> players;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
