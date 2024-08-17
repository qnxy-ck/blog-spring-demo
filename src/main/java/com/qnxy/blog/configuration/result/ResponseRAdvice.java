package com.qnxy.blog.configuration.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnxy.blog.core.enums.CommonResultStatusCodeE;
import com.qnxy.blog.core.ex.BizException;
import com.qnxy.blog.data.R;
import lombok.RequiredArgsConstructor;
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
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseRAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getNestedParameterType().equals(R.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (returnType.getNestedParameterType().equals(String.class)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return this.objectMapper.writeValueAsString(R.suc(body));
            } catch (JsonProcessingException e) {
                throw new BizException(e, CommonResultStatusCodeE.JSON_CONVERSION_EXCEPTION);
            }
        }

        return R.suc(body);
    }
}
