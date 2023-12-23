package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Steveny
 * @since 2023/10/6
 */
public class EmailCodeException extends SalusException {
    public EmailCodeException(HttpCodeEnum enums) {
        super(enums);
    }
}
