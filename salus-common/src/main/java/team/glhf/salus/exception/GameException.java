package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Steveny
 * @since 2023/11/1
 */
public class GameException extends SalusException {
    public GameException(HttpCodeEnum enums) {
        super(enums);
    }
}
