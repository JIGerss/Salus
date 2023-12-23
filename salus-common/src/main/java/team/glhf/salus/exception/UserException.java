package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Steveny
 * @since 2023/10/6
 */
public class UserException extends SalusException {
    public UserException(HttpCodeEnum enums) {
        super(enums);
    }
}
