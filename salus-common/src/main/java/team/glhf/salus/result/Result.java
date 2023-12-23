package team.glhf.salus.result;

import lombok.Data;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.utils.SalusUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * Result
 *
 * @author Steveny
 * @since 2023/9/22
 */
@Data
@SuppressWarnings("unused")
public final class Result<T> implements Serializable {
    private final Integer code;

    private final String message;

    private final String timestamp;

    private final T data;

    @Serial
    private static final long serialVersionUID = 1919081114514233L;

    // =============================  Constructor  =============================

    private Result(Integer code, String message, String timestamp, T data) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    // ===========================  InstanceMethod  =============================

    public boolean cEquals(Integer code) {
        return null != code && code.equals(this.code);
    }

    public boolean cEquals(HttpCodeEnum enums) {
        return enums.getCode().equals(this.code);
    }

    public boolean cEquals(Result<T> result) {
        return null != result && null != result.getCode() && null != this.code
                && this.code.equals(result.getCode());
    }

    // =============================  StaticMethod  =============================

    private static <T> ResponseResultBuilder<T> builder() {
        return new ResponseResultBuilder<>();
    }

    public static <T> Result<T> okResult(T data) {
        return Result.<T>builder()
                .httpCodeEnum(HttpCodeEnum.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> Result<T> okResult() {
        return Result.<T>builder()
                .httpCodeEnum(HttpCodeEnum.SUCCESS)
                .build();
    }

    public static <T> Result<T> errorResult(HttpCodeEnum enums) {
        return Result.<T>builder()
                .httpCodeEnum(enums)
                .build();
    }

    public static <T> Result<T> errorResult(HttpCodeEnum enums, String message) {
        return Result.<T>builder()
                .httpCodeEnum(enums)
                .message(message)
                .build();
    }

    public static <T> Result<T> errorResult(Integer code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> Result<T> errorResult(Integer code, String message, T data) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <R> Result<R> cClone(Result<?> result) {
        return Result.<R>builder().code(result.getCode()).message(result.getMessage()).build();
    }

    // =============================  Builder  =============================

    private final static class ResponseResultBuilder<T> {
        private Integer code;
        private String message;
        private T data;

        private ResponseResultBuilder() {
        }

        public ResponseResultBuilder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public ResponseResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseResultBuilder<T> httpCodeEnum(HttpCodeEnum enums) {
            this.code = enums.getCode();
            this.message = enums.getMessage();
            return this;
        }

        public Result<T> build() {
            return new Result<>(code, message, SalusUtil.date(), data);
        }
    }
}
