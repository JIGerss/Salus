package team.glhf.salus.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.utils.UserUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * AutoParameterFillUserIdAop
 *
 * @author Steveny
 * @since 2023/11/3
 */
@Aspect
@Component
public class AutoParameterFillAop {

    @Around(value = "@annotation(team.glhf.salus.annotation.JwtVerify)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        HandlerMethod handlerMethod = new HandlerMethod(joinPoint.getTarget(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        JwtVerify annotation = method.getAnnotation(JwtVerify.class);
        if (annotation.autoFill()) {
            for (MethodParameter parameter : methodParameters) {
                Class<?> aClass = parameter.getParameter().getType();
                if (aClass.equals(String.class)) {
                    args[parameter.getParameterIndex()] = UserUtil.getUserId();
                    break;
                } else {
                    for (Field field : aClass.getDeclaredFields()) {
                        if ("userId".equals(field.getName())) {
                            field.setAccessible(true);
                            field.set(args[parameter.getParameterIndex()], UserUtil.getUserId());
                        }
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
