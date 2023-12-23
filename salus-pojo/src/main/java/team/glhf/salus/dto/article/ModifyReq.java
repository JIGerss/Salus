package team.glhf.salus.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Data
@Builder
public class ModifyReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String articleId;

    private String title;

    @Length(max = 2000, message = "content cannot be longer than 2000 alphas")
    private String content;

    private List<String> images;

    private List<String> tags;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
