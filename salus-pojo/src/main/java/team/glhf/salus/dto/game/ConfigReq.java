package team.glhf.salus.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/11/16
 */
@Data
@Builder
public class ConfigReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String key;

    @Range(min = 1200, max = 3600, message = "param cannot be more than 3600 or less than 1200")
    @NotNull(message = "param cannot be null")
    private Integer time;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
