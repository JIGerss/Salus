package team.glhf.salus.utils;

import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * JwtUtil
 *
 * @author Felix
 * @since 2023/11/1
 */
public class JwtUtil {
    private static final byte[] KEY = "GLHF".getBytes();

    public static boolean verify(String token) {
        if (StringUtils.isBlank(token)) return false;
        return JWT.of(token).setKey(KEY).verify();
    }

    public static String verifyAndGetUserId(String token) {
        if (!verify(token)) return null;
        // 解析数据
        JWT jwt = JWT.of(token);
        // 返回用户ID
        return jwt.getPayload("id").toString();
    }
}
