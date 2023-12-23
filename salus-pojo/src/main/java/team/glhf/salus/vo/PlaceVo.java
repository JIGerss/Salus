package team.glhf.salus.vo;

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
public class PlaceVo implements Serializable {

    private String id;

    private String name;

    private List<String> images;

    private String position;

    private String description;

    private String businessTime;

    private String telephone;

    private Double point;

    private List<PlaceCommentVo> comments;

    private String formattedAddress;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
