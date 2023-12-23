package team.glhf.salus.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Steveny
 * @since 2023/10/30
 */
@Data
@Builder
public class CreateReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String title;

    @NotBlank(message = "content cannot be null")
    @Length(max = 2000, message = "content cannot be longer than 2000 alphas")
    private String content;

    @NotNull(message = "param cannot be null, you can use a '[]' instead")
    private List<String> tags;

    @Pattern(regexp = "^[0-9]{1,4}.?[0-9]{0,6},[0-9]{1,4}.?[0-9]{0,6}$", message = "param cannot march the pattern")
    private String position;

    private List<MultipartFile> images;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
