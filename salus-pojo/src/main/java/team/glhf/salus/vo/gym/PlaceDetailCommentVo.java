package team.glhf.salus.vo.gym;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * article_own_comment database mapper
 *
 * @author Felix
 * @since 2023/11/09
 */
@Data
@Builder
public class PlaceDetailCommentVo implements Serializable {

    private String userId;

    private String nickname;

    private String comment;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
