package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * Salus Business Exception
 *
 * @author Steveny
 * @since 2023/10/6
 */
@SuppressWarnings("unused")
public class SalusException extends RuntimeException {
    private final Integer code;

    public SalusException(HttpCodeEnum enums) {
        super(enums.getMessage());
        this.code = enums.getCode();
    }

    public SalusException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
