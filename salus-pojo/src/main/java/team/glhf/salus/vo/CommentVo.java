package team.glhf.salus.vo;

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
public class CommentVo implements Serializable {

    private String userId;

    private String comment;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}