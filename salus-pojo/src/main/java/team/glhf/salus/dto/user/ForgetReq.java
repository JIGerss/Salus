package team.glhf.salus.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/10/15
 */
@Data
@Builder
public class ForgetReq implements Serializable {
    @Email(message = "email pattern invalid")
    @NotBlank(message = "param cannot be null")
    private String email;

    @NotBlank(message = "verifyCode cannot be null")
    @Length(min = 5, max = 5, message = "verifyCode must be 5 digits")
    private String verifyCode;

    @NotBlank(message = "password cannot be null")
    @Length(max = 20, message = "password cannot be longer than 20 digits")
    private String newPassword;

//    @NotBlank(message = "confirm password cannot be null")
//    @Length(min = 8, max = 12, message = "password must be 8-12 digits")
//    private String confirmPassword;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
