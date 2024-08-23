package com.qnxy.blog.configuration.auth;

import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentAuthUserIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationConfigurationProperties authenticationConfigurationProperties;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getNestedParameterType().equals(CurrentAuthUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String authToken = webRequest.getHeader(this.authenticationConfigurationProperties.getRequestHeaderTokenName());

        if (!StringUtils.hasText(authToken)) {
            log.warn("未获取到 jwt token 信息, 请确认是否在需授权控制器中使用 CurrentAuthUserId 对象注入!");
            return CurrentAuthUserId.create(null);
        }

        final Long userId = this.userService.checkJwtTokenAndParse(authToken);
        return CurrentAuthUserId.create(userId);
    }
}
