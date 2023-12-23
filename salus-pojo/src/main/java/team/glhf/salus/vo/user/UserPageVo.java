package team.glhf.salus.vo.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/12
 */
@Data
@Builder
public class UserPageVo implements Serializable {

    private String userId;

    private String nickname;

    private String avatar;

    private Long fans;

    private Long subscriptions;

    private List<UserArticleVo> ownedArticles;

    private List<UserArticleVo> likedArticles;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
