package team.glhf.salus.vo.detail;

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
public class DetailCommentVo implements Serializable {

    private DetailCommentUserVo commentUser;

    private String comment;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
