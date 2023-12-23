package team.glhf.salus.dto.place;

import jakarta.validation.constraints.Pattern;
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
public class GetNearbyReq implements Serializable {

    @Pattern(regexp = "^[0-9.]*[,;][0-9.]*$", message = "param cannot march the pattern")
    private String position;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
