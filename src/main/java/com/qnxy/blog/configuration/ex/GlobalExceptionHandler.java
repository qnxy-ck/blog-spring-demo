package com.qnxy.blog.configuration.ex;

import com.qnxy.blog.core.BizException;
import com.qnxy.blog.data.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import static com.qnxy.blog.core.CommonResultStatusCodeE.*;

/**
 * @author Qnxy
 */
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionConfigurationProperties exceptionConfigurationProperties;

    private static final Map<Class<? extends Exception>, BiFunction<Exception, String, R<?>>> CLASS_EXCEPTION_PROCESSOR_MAP = new ConcurrentHashMap<>() {{
        
        /*
            自定义异常处理
         */
        put(BizException.class, (e, stackTraceInfo) -> R.ofBizEx((BizException) e, stackTraceInfo));
        
        /*
            参数校验异常处理
            @RequestBody 
         */
        put(MethodArgumentNotValidException.class, (e, stackTraceInfo) -> {
            List<String> errorMessageList = ((MethodArgumentNotValidException) e).getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(it -> it != null && !it.isEmpty())
                    .toList();

            return R.result(errorMessageList, PARAMETER_VERIFICATION_FAILED);
        });


//        put(ConstraintViolationException.class, (e, stackTraceInfo) -> R.result(e.getMessage(), PARAMETER_VERIFICATION_FAILED));

        /*
            参数列表校验发生异常
         */
        put(HandlerMethodValidationException.class, (e, stackTraceInfo) -> {
            List<String> list = ((HandlerMethodValidationException) e).getAllErrors()
                    .stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .filter(it -> it != null && !it.isEmpty())
                    .toList();

            return R.result(list, PARAMETER_VERIFICATION_FAILED);
        });

        /*
            资源不存在异常
         */
        put(NoResourceFoundException.class, (e, s) -> {
            NoResourceFoundException noResourceFoundException = (NoResourceFoundException) e;
            return R.exStackTrace(ACCESS_RESOURCE_DOES_NOT_EXIST, s, noResourceFoundException.getResourcePath());
        });

        /*
            MyBatis 框架发出的异常
         */
        put(MyBatisSystemException.class, (e, s) -> {
            if (((NestedRuntimeException) e).getRootCause() instanceof BizException bizException) {
                return R.ofBizEx(bizException, s);
            }
            return R.exStackTrace(UNKNOWN_EXCEPTION, s);
        });


    }};


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public R<?> exception(Exception e, HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String msg = e instanceof BizException
                ? ((BizException) e).getStatus().getCode() + ":" + e.getMessage()
                : e.getMessage();

        log.error("请求路径: {} -> 发生错误: {}", requestURI, msg, e);

        final String stackTraceInfo = enableExStackTrace() ? stackTrace(e) : null;

        return Optional.ofNullable(CLASS_EXCEPTION_PROCESSOR_MAP.get(e.getClass()))
                .map(it -> it.apply(e, stackTraceInfo))
                .orElseGet(() -> R.exStackTrace(UNKNOWN_EXCEPTION, stackTraceInfo));
    }


    private boolean enableExStackTrace() {
        return this.exceptionConfigurationProperties.getEnableExceptionStackTrace() == Boolean.TRUE;
    }

    private static String stackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }
}
