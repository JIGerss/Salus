package team.glhf.salus.enumeration;

/**
 * Http Errors
 *
 * @author Steveny
 * @since 2023/6/24
 */
public enum HttpCodeEnum {
    // Success ==> 200
    SUCCESS(200, "操作成功"),

    // Timeout ==> 400
    TIME_OUT(400, "系统超时"),

    // Login Error ==> 1~50
    NEED_LOGIN_AFTER(1, "需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2, "密码错误"),
    ACCOUNT_NOT_FOUND(3, "账号不存在"),
    ACCOUNT_DISABLED(4, "账号被禁用"),
    NUMBER_CODE_NOT_EQUAL(4, "验证码不匹配"),
    NUMBER_CODE_NOT_EXIST(5, "请先发送验证码"),
    ACCOUNT_PASSWORD_ERROR(6, "账号或密码错误"),

    // Token ==> 50~100
    TOKEN_INVALID(50, "无效的TOKEN"),
    TOKEN_REQUIRE(51, "TOKEN是必须的"),
    TOKEN_EXPIRED_TIME(52, "TOKEN已过期"),
    TOKEN_ALGORITHM_SIGNATURE_INVALID(53, "TOKEN的算法或签名不对"),
    TOKEN_CANT_EMPTY(54, "TOKEN不能为空"),

    // Sign Verification ==> 100~120
    SIGN_INVALID(100, "无效的SIGN"),
    SIG_TIMEOUT(101, "SIGN已过期"),

    // Args Error ==> 500~1000
    PARAM_REQUIRE(500, "缺少参数"),
    PARAM_INVALID(501, "无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502, "图片格式有误"),
    INVALID_ID(503, "该ID查无数据"),
    SERVER_ERROR(503, "服务器内部错误"),
    SERVER_BUSY(504, "服务器繁忙"),
    USER_NOT_REGISTERED(505, "用户未注册"),
    USER_REGISTERED(506, "用户已注册"),
    REQUEST_ERROR(507, "请求出错"),

    // Data Error ==> 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    DATA_PART_NOT_EXIST(1002, "前端请求，部分必要数据不能为空"),
    DATA_ALL_NOT_EXIST(1002, "前端请求，数据完全为空"),
    DATA_PART_ILLEGAL(1003, "部分数据不合法"),
    DATA_UPLOAD_ERROR(1004, "服务器上传失败"),
    PHONE_ILLEGAL(1005, "手机号为空或者格式不合法"),
    PASSWORD_ILLEGAL(1006, "密码为空或者格式不合法"),
    NUMBER_CODE_ILLEGAL(1007, "未获取验证码或者格式不合法"),
    TOKEN_DATA_INVALID(1008, "token里的荷载数据有误"),
    USERNAME_PASSWORD_ILLEGAL(1009, "账号或密码为空或长度过长"),
    EMAIL_SEND_ERROR(1010, "发送邮件失败，请重试"),
    SMS_SEND_ERROR(1011, "发送短信失败，请重试"),
    GET_LOCATION_ERROR(1012, "获取地址失败，请检查参数是否正确"),

    // Article Error
    ARTICLE_NOT_EXIST(1101, "文章不存在，请检查参数是否正确"),
    ARTICLE_ALREADY_LIKED(1102, "文章已被点赞"),
    ARTICLE_NOT_LIKED(1103, "文章没有被点赞"),
    ARTICLE_PERMISSION_ERROR(1104, "你没有权限操作该文章"),
    UPLOAD_ERROR(1105, "上传文件失败，可能文件太大了，请重试"),

    // User Error
    USER_ALREADY_SUBSCRIBED(1201, "用户已被关注"),
    USER_NOT_SUBSCRIBED(1202, "用户没有被关注"),
    USER_SUBSCRIBED_ITSELF(1203, "用户不能关注自己"),

    //Place Error
    PLACE_NOT_EXIST(1301,"场所不存在，请检查参数是否正确"),
    PLACE_ALREADY_POINTED(1302, "场所已被该用户评分"),
    PLACE_NOT_POINTED(1303, "场所没有被该用户评分"),

    // Game Error
    FAIL_TO_JOIN_GAME(1401, "加入失败，请检查参数是否正确"),
    SEND_MESSAGE_ERROR(1402, "消息发送失败，请检查参数是否正确"),
    GAME_NOT_EXIST(1403, "游戏不存在，请检查参数是否正确"),
    GAME_PERMISSION_ERROR(1404, "你不是房主"),
    GAME_ALREADY_STARTED(1405, "游戏已经开始"),
    USER_NOT_JOIN_GAME(1406, "你尚未加入该游戏"),
    GAME_CONFIGURATION_ERROR(1407, "玩家尚未选择角色，或者游戏时间未配置"),
    GAME_NO_BOTH_ROLES(1408, "缺少至少一个猫或至少一个老鼠"),
    GAME_STAGE_ERROR(1409, "游戏还未开始或者已经结束"),
    GAME_CAUGHT_MOUSE(1410, "你已被抓住");

    private final Integer code;
    private final String message;

    HttpCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
