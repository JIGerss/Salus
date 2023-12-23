package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Felix
 * @since 2023/11/5
 */
public class CommonException extends SalusException{
    public CommonException(HttpCodeEnum enums) {
        super(enums);
    }
}
