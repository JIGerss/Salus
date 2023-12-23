package team.glhf.salus.dto.page;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/11/16
 */
@Data
@Builder
public class PlaceListReq implements Serializable {

//    @Null(message = "cannot use userId to access")
//    private String userId;

    @Pattern(regexp = "^[0-9]{1,4}.?[0-9]{0,6},[0-9]{1,4}.?[0-9]{0,6}$", message = "param cannot march the pattern")
    private String position;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
