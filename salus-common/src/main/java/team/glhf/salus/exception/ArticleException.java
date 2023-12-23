package team.glhf.salus.exception;

import team.glhf.salus.enumeration.HttpCodeEnum;

/**
 * @author Steveny
 * @since 2023/11/1
 */
public class ArticleException extends SalusException {
    public ArticleException(HttpCodeEnum enums) {
        super(enums);
    }
}
