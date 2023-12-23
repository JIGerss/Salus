package team.glhf.salus.handler;

import jakarta.servlet.ServletException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import team.glhf.salus.enumeration.HttpCodeEnum;
import team.glhf.salus.exception.SalusException;
import team.glhf.salus.result.Result;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Global exception handler
 *
 * @author Steveny
 * @since 2023/6/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public <T> Result<T> doRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.errorResult(HttpCodeEnum.SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public <T> Result<T> doBindException(BindException exception) {
        List<FieldError> allErrors = exception.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, allErrors.size()).forEach(i -> {
            FieldError error = allErrors.get(i);
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage());
            if (i != allErrors.size() - 1)
                sb.append(", ");
        });
        return Result.errorResult(HttpCodeEnum.PARAM_INVALID, sb.toString());
    }

    @ExceptionHandler(SalusException.class)
    public <T> Result<T> doMessagingException(SalusException e) {
        return Result.errorResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> Result<T> doHttpMessageNotReadableException() {
        return Result.errorResult(HttpCodeEnum.PARAM_INVALID);
    }

    @ExceptionHandler(MultipartException.class)
    public <T> Result<T> doMultipartException() {
        return Result.errorResult(HttpCodeEnum.UPLOAD_ERROR);
    }

    @ExceptionHandler(ServletException.class)
    public <T> Result<T> doServletException() {
        return Result.errorResult(HttpCodeEnum.REQUEST_ERROR);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public <T> Result<T> doServletRequestBindingException() {
        return Result.errorResult(HttpCodeEnum.PARAM_REQUIRE);
    }
}
