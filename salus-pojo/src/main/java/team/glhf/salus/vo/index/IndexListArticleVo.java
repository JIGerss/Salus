package team.glhf.salus.vo.index;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/11/16
 */
@Data
@Builder
public class IndexListArticleVo implements Serializable {

    private List<IndexArticleVo> articles;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
