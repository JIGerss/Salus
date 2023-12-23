package team.glhf.salus.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/10/30
 */
@Data
@Builder
public class LocationReq implements Serializable {

    @NotBlank(message = "param cannot be null")
    @Pattern(regexp = "^[0-9]{1,4}.?[0-9]{0,6},[0-9]{1,4}.?[0-9]{0,6}$", message = "param cannot march the pattern")
    private String position;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
