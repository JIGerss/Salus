package team.glhf.salus.vo.gym;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Place database mapper
 *
 * @author Steveny
 * @since 2023/11/21
 */

@Data
@Builder
public class PlacePageVo implements Serializable {

    List<PlacePageGymVo> gyms;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
