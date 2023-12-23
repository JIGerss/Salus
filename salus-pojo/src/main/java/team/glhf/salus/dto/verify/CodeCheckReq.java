package team.glhf.salus.dto.verify;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/9/27
 */
@Data
@Builder
public class CodeCheckReq implements Serializable {
    private String key;

    private String verifyCode;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
