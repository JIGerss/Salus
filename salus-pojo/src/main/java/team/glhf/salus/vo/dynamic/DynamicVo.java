package team.glhf.salus.vo.dynamic;

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
public class DynamicVo implements Serializable {

    private List<DynamicArticleVo> articles;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
