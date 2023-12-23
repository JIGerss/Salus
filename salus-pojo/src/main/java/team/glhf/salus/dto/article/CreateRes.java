package team.glhf.salus.dto.article;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Data
@Builder
public class CreateRes implements Serializable {
    private String articleId;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
