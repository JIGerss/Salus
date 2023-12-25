package team.glhf.salus.vo.gym;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Place database mapper
 *
 * @author Steveny
 * @since 2023/11/21
 */

@Data
@Builder
public class PlacePageGymVo implements Serializable {

    private String id;

    private String name;

    private String cover;

    private Double point;

    private String formattedAddress;

    private Integer comments;

    private Double distance;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
