package team.glhf.salus.dto.user;

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
public class ForgetRes implements Serializable {
    private String userId;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
