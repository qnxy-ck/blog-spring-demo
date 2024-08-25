package com.qnxy.blog.configuration.result;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.CommonResultStatusCodeE;
import com.qnxy.blog.data.R;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 返回数据统一包装为 {@link R}
 *
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseRAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //noinspection unchecked
        final R<Object> r = body instanceof R ? (R<Object>) body : R.suc(body);

        final String message = this.messageSource.getMessage(r.getStatusCode(), r.getArgs(), LocaleContextHolder.getLocale());
        final PrivateR privateR = new PrivateR(r, message);

        return returnType.getNestedParameterType().equals(String.class)
                ? returnValueToJson(response, privateR)
                : privateR;
    }

    private Object returnValueToJson(ServerHttpResponse response, PrivateR privateR) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            return this.objectMapper.writeValueAsBytes(privateR);
        } catch (JsonProcessingException e) {
            throw new BizException(e, CommonResultStatusCodeE.JSON_CONVERSION_EXCEPTION);
        }
    }

    @Data
    private static class PrivateR {

        /**
         * 返回时时间戳
         */
        private final long timestamp = System.currentTimeMillis();
        @JsonUnwrapped
        private R<?> data;
        /**
         * 返回状态信息
         */
        private String statusMsg;

        public PrivateR(R<?> data, String message) {
            this.data = data;
            this.statusMsg = message;
        }
    }
}
