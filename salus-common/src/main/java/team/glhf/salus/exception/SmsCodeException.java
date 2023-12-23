package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Steveny
 * @since 2023/10/13
 */
public class SmsCodeException extends SalusException {
    public SmsCodeException(HttpCodeEnum enums) {
        super(enums);
    }

    public SmsCodeException(Integer code, String message) {
        super(code, message);
    }
}
