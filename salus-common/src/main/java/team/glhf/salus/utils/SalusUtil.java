package team.glhf.salus.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;
import java.util.stream.IntStream;

/**
 * SalusUtil
 *
 * @author Steveny
 * @since 2023/9/23
 */
public final class SalusUtil {
    private static final char[] charset = new char[10 + 26];

    static {
        IntStream.range(0, 10).forEach(i -> charset[i] = (char) (i + '0'));
        IntStream.range(0, 26).forEach(i -> charset[i + 10] = (char) (i + 'A'));
    }

    public static String getVerifyCode(int digits, boolean alpha) {
        final int RANGE = alpha ? 26 : 10;
        final StringBuilder sb = new StringBuilder();
        IntStream.range(0, digits).forEach(i -> sb.append(charset[RandomUtil.randomInt(RANGE)]));
        return sb.toString();
    }

    public static String offsetTime(int offset, String dateField) {
        dateField = dateField.substring(0, dateField.length() - 1);
        return DateUtil.formatDateTime(DateUtil.offset(new Date(), DateField.valueOf(dateField), offset));
    }

    public static String date() {
        return DateUtil.formatDateTime(new Date(System.currentTimeMillis()));
    }

    public static int compareTime(String a, String b) {
        return DateUtil.parse(b).compareTo(DateUtil.parse(a));
    }
}
