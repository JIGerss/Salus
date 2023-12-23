package team.glhf.salus.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/9/22
 */
@Data
@Builder
public class LoginReq implements Serializable {
    @Email(message = "email pattern invalid")
    @NotBlank(message = "param cannot be null")
    private String email;

    @NotBlank(message = "password cannot be null")
    @Length(max = 20, message = "password cannot be longer than 20 digits")
    private String password;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
