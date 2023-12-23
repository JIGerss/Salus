package team.glhf.salus.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import team.glhf.salus.annotation.AutoOperateGame;
import team.glhf.salus.service.GameService;
import team.glhf.salus.websocket.Game;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * AutoOperateGameAop
 *
 * @author Steveny
 * @since 2023/12/11
 */
@Aspect
@Component
public class AutoOperateGameAop {
    private final GameService gameService;

    public AutoOperateGameAop(GameService gameService) {
        this.gameService = gameService;
    }

    @Around(value = "@annotation(team.glhf.salus.annotation.AutoOperateGame)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        HandlerMethod handlerMethod = new HandlerMethod(joinPoint.getTarget(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        AutoOperateGame annotation = method.getAnnotation(AutoOperateGame.class);
        // 根据参数中的key获取Game对象
        String key = null;
        for (MethodParameter parameter : methodParameters) {
            Class<?> aClass = parameter.getParameter().getType();
            if (aClass.equals(String.class)) {
                key = args[parameter.getParameterIndex()].toString();
                break;
            } else {
                for (Field field : aClass.getDeclaredFields()) {
                    if ("key".equals(field.getName())) {
                        field.setAccessible(true);
                        key = field.get(args[parameter.getParameterIndex()]).toString();
                        break;
                    }
                }
                if (null != key) {
                    break;
                }
            }
        }
        Game game = gameService.getGameByKey(key);
        // 自动填充Game参数
        if (annotation.autoFill()) {
            for (MethodParameter parameter : methodParameters) {
                Class<?> aClass = parameter.getParameter().getType();
                if (aClass.equals(Game.class)) {
                    args[parameter.getParameterIndex()] = game;
                    break;
                }
            }
        }
        Object proceeded = joinPoint.proceed(args);
        // 自动进行全局广播
        if (annotation.autoBroadcast()) {
            gameService.sendGameInfo(game);
        }
        return proceeded;
    }
}
