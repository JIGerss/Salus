package team.glhf.salus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Felix
 * @since 2023/12/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoOperateGame {

    boolean autoBroadcast() default true;

    boolean autoFill() default true;
}
