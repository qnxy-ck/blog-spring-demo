package com.qnxy.blog.configuration.ex;

import com.qnxy.blog.core.ex.BizException;
import com.qnxy.blog.data.R;
import com.qnxy.blog.data.enums.CommonResultStatusCodeE;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Qnxy
 */
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionConfigurationProperties exceptionConfigurationProperties;


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public <D> R<D> exception(Exception e, HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        String msg = e instanceof BizException
                ? ((BizException) e).getStatus().getCode() + ":" + e.getMessage()
                : e.getMessage();

        log.error("请求路径: {} -> 发生错误: {}", requestURI, msg);


        if (e instanceof BizException be) {
            return R.ofBizEx(null, be);
        }

        if (enableExStackTrace()) {
            return R.fail(CommonResultStatusCodeE.STACK_INFO, stackTrace(e));
        }

        return R.fail(CommonResultStatusCodeE.UNKNOWN_EXCEPTION);
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
