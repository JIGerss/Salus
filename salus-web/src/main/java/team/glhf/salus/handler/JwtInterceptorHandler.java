package team.glhf.salus.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import team.glhf.salus.annotation.JwtVerify;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.UserException;
import team.glhf.salus.service.UserService;
import team.glhf.salus.utils.JwtUtil;
import team.glhf.salus.utils.UserUtil;

import java.lang.reflect.Method;

/**
 * @author Felix
 * @since 2023/11/1
 */
@Slf4j
@Component
public class JwtInterceptorHandler implements HandlerInterceptor {
    private final UserService userService;

    public JwtInterceptorHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String token = request.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // 检查是否有JwtVerify注释，有则跳过认证
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(JwtVerify.class)) {
            JwtVerify userLoginToken = method.getAnnotation(JwtVerify.class);
            if (!userLoginToken.required() || !StringUtils.isNotBlank(token)) {
                throw new UserException(HttpCodeEnum.TOKEN_REQUIRE);
            }
            try {
                String userId = JwtUtil.verifyAndGetUserId(token);
                if (!userService.checkHasUser(userId, null)) {
                    throw new UserException(HttpCodeEnum.TOKEN_INVALID);
                }
                UserUtil.setUserId(userId);
            } catch (RuntimeException e) {
                throw new UserException(HttpCodeEnum.TOKEN_INVALID);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        UserUtil.remove();
    }
}
