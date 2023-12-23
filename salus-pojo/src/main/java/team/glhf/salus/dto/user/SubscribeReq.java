package team.glhf.salus.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Felix
 * @since 2023/10/15
 */
@Data
@Builder
public class SubscribeReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String toUserId;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
