package team.glhf.salus.vo.index;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/11/12
 */
@Data
@Builder
public class IndexPlaceVo implements Serializable {

    private String id;

    private String name;

    private String cover;

    private String position;

    private Double point;

    private Double distance;

    private String formattedAddress;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
