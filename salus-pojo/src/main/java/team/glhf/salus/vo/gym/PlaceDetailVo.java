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
public class PlaceDetailVo implements Serializable {

    private String name;

    private List<String> images;

    private String position;

    private String description;

    private String businessTime;

    private String telephone;

    private Double point;

    private List<PlaceDetailCommentVo> comments;

    private String formattedAddress;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
