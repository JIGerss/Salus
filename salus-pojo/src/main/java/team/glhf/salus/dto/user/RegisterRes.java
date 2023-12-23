package team.glhf.salus.dto.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/9/22
 */
@Data
@Builder
public class RegisterRes implements Serializable {
    private String userId;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
