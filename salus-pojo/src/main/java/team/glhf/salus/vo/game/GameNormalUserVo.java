package team.glhf.salus.vo.game;

import lombok.Builder;
import lombok.Data;
import team.glhf.salus.enumeration.GameRoleEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/11/09
 */
@Data
@Builder
public class GameNormalUserVo implements Serializable {

    private String userId;

    private String nickname;

    private String avatar;

    private Boolean isHost;

    private Double longitude;

    private Double latitude;

    private GameRoleEnum role;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
