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
public class IndexArticleUserVo implements Serializable {

    private String avatar;

    private String nickname;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
