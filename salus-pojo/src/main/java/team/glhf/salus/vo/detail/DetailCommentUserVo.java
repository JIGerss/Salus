package team.glhf.salus.vo.detail;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/12/1
 */
@Data
@Builder
public class DetailCommentUserVo implements Serializable {

    private String avatar;

    private String nickname;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
