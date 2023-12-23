package team.glhf.salus.utils;

import java.util.Objects;


/**
 * UserUtil
 *
 * @author Felix
 * @since 2023/11/1
 */
public class UserUtil {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(String id) {
        THREAD_LOCAL.set(id);
    }

    public static String getUserId() {
        String id = THREAD_LOCAL.get();
        return Objects.requireNonNullElse(id, "");
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
