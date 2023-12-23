package team.glhf.salus.dto.verify;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Steveny
 * @since 2023/9/23
 */
@Data
@Builder
public class SmsCodeRes implements Serializable {
    private String phone;

    private String refreshTime;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;
}
