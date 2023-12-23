package team.glhf.salus.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
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
public class StartGameReq implements Serializable {

    @Null(message = "cannot use userId to access")
    private String userId;

    @NotBlank(message = "param cannot be null")
    private String key;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
