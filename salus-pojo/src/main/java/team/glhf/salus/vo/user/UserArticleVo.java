package team.glhf.salus.vo.user;

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
public class UserArticleVo implements Serializable {

    private String articleId;

    private String title;

    private String cover;

    private String createTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
