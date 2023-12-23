package team.glhf.salus.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Redis verifyCode mapper
 *
 * @author Steveny
 * @since 2023/9/23
 */
@Data
@Builder
public class Code implements Serializable {
    private String code;

    private String refreshTime;

    private Integer chance;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
