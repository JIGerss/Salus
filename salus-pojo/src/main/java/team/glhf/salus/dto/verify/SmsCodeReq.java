package team.glhf.salus.dto.verify;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class SmsCodeReq implements Serializable {
    @Pattern(regexp = "^1[0-9]{10}$", message = "phone pattern invalid")
    @NotBlank(message = "param cannot be null")
    private String phone;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
