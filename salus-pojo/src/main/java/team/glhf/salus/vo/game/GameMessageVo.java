package team.glhf.salus.vo.game;

import lombok.Builder;
import lombok.Data;
import team.glhf.salus.enumeration.GameMessageType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/11/09
 */
@Data
@Builder
public class GameMessageVo implements Serializable {

    private String message;

    private GameMessageType messageType;

    private Integer time;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
