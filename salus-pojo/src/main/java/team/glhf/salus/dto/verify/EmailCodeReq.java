package team.glhf.salus.dto.verify;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/9/23
 */
@Data
@Builder
public class EmailCodeReq implements Serializable {
    @Email(message = "email pattern invalid")
    @NotBlank(message = "param cannot be null")
    private String email;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
