package com.qnxy.blog.configuration.ex;

import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.VerificationExpectations;
import com.qnxy.blog.data.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.qnxy.blog.core.CommonResultStatusCodeE.*;

/**
 * @author Qnxy
 */
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements InitializingBean {

    private static final Map<Class<? extends Exception>, Function<Exception, R<?>>> CLASS_EXCEPTION_PROCESSOR_MAP = new ConcurrentHashMap<>();

    private final MessageSource messageSource;

    private static void buildExceptionHandlerMapping() {

        // 自定义异常处理
        addExceptionHandler(BizException.class, R::ofBizEx);

        // 参数校验异常处理 @RequestBody
        addExceptionHandler(MethodArgumentNotValidException.class, e -> {
            List<String> errorMessageList = e.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(it -> it != null && !it.isEmpty())
                    .toList();

            return R.result(errorMessageList, PARAMETER_VERIFICATION_FAILED);
        });


        // 参数列表校验发生异常
        addExceptionHandler(HandlerMethodValidationException.class, e -> {
            List<String> list = e.getAllErrors()
                    .stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .filter(it -> it != null && !it.isEmpty())
                    .toList();

            return R.result(list, PARAMETER_VERIFICATION_FAILED);
        });

        // 资源不存在异常
        addExceptionHandler(NoResourceFoundException.class, e -> R.fail(ACCESS_RESOURCE_DOES_NOT_EXIST, e.getResourcePath()));

        // 请求方式不支持异常
        addExceptionHandler(HttpRequestMethodNotSupportedException.class, e -> R.fail(UNSUPPORTED_REQUEST_METHOD, e.getMethod()));

        // MyBatis 框架发出的异常
        addExceptionHandler(NoResourceFoundException.class, e -> {
            if (e.getRootCause() instanceof BizException bizException) {
                return R.ofBizEx(bizException);
            }
            return R.fail(UNKNOWN_EXCEPTION);
        });

        // 处理 HttpMessageNotReadableException 异常
        addExceptionHandler(HttpMessageNotReadableException.class, e -> {
            if (e.getRootCause() instanceof BizException bizException) {
                return R.ofBizEx(bizException);
            }

            return R.fail(UNKNOWN_EXCEPTION);
        });

        /*
            VerificationExpectations.NestedException 处理
            去除嵌套的异常在当前 Map 中取出已知的异常处理器进行处理
            如果没有找到抛出未知异常
         */
        addExceptionHandler(VerificationExpectations.NestedException.class, e -> {
            final Throwable cause = e.getCause();
            return Optional.ofNullable(cause)
                    .map(it -> CLASS_EXCEPTION_PROCESSOR_MAP.get(it.getClass()))
                    .map(it -> it.apply((Exception) cause))
                    .orElseGet(() -> R.fail(UNKNOWN_EXCEPTION));
        });

    }

    /**
     * 添加异常处理映射关系
     *
     * @param exceptionType 异常类型
     * @param function      处理器
     * @param <E>           异常泛型
     */
    private static <E extends Exception> void addExceptionHandler(Class<E> exceptionType, Function<E, R<?>> function) {
        //noinspection unchecked
        CLASS_EXCEPTION_PROCESSOR_MAP.put(exceptionType, (Function<Exception, R<?>>) function);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public R<?> exception(Exception e, HttpServletRequest request) {

        printException(e, request);

        return Optional.ofNullable(CLASS_EXCEPTION_PROCESSOR_MAP.get(e.getClass()))
                .map(it -> it.apply(e))
                .orElseGet(() -> R.fail(UNKNOWN_EXCEPTION));
    }

    private void printException(Exception e, HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        final Throwable printException;
        final String msg;
        if (e instanceof BizException bizException) {
            printException = bizException.getCause();

            final String code = bizException.getCode();
            final String message = this.messageSource.getMessage(code, bizException.getArgs(), Locale.getDefault());
            msg = code + " -> " + message;
        } else {
            printException = e;
            msg = e.getMessage();
        }

        log.error("请求路径: {} -> 发生错误: {}", requestURI, msg, printException);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildExceptionHandlerMapping();
    }


}
