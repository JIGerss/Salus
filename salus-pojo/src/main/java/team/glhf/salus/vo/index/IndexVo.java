package team.glhf.salus.vo.index;

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
public class IndexVo implements Serializable {

    private List<IndexTagVo> topics;

    private List<IndexArticleVo> articles;

    private List<IndexPlaceVo> places;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
