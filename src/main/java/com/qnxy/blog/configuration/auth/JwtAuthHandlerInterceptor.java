package com.qnxy.blog.configuration.auth;

import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.CommonResultStatusCodeE;
import com.qnxy.blog.core.annotations.IgnoreAuth;
import com.qnxy.blog.core.annotations.NoIgnoreAuth;
import com.qnxy.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@Component
@RequiredArgsConstructor
public class JwtAuthHandlerInterceptor implements HandlerInterceptor {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final AuthenticationConfigurationProperties authenticationConfigurationProperties;
    private final UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        /*
            静态资源不进行校验, 直接放行
         */
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }


        final RequestMethod requestMethod = RequestMethod.resolve(request.getMethod());
        final String requestURI = request.getRequestURI();

        // 校验项目配置无需校验的url
        if (checkIgnoreUri(requestMethod, requestURI)) {
            return true;
        }

        // 校验当前方法是否为需要放行的
        if (checkIgnoreMethod(handlerMethod)) {
            return true;
        }

        final String authToken = request.getHeader(this.authenticationConfigurationProperties.getRequestHeaderTokenName());
        if (!StringUtils.hasText(authToken)) {
            throw new BizException(CommonResultStatusCodeE.UNAUTHORIZED_ACCESS);
        }

        this.userService.checkJwtTokenAndParse(authToken);
        return true;
    }


    private boolean checkIgnoreUri(RequestMethod requestMethod, String requestURI) {
        final Map<RequestMethod, List<String>> ignoreAuthenticationUrl = this.authenticationConfigurationProperties.getIgnoreAuthenticationUrl();
        if (ignoreAuthenticationUrl == null) {
            return false;
        }

        for (RequestMethod method : ignoreAuthenticationUrl.keySet()) {
            if (requestMethod != method) {
                continue;
            }
            for (String pattern : ignoreAuthenticationUrl.get(method)) {
                if (ANT_PATH_MATCHER.match(pattern, requestURI)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkIgnoreMethod(HandlerMethod requestMethod) {
        IgnoreAuth beanIgnoreAuth = requestMethod.getBeanType().getAnnotation(IgnoreAuth.class);

        final Method method = requestMethod.getMethod();
        IgnoreAuth ignoreAuth = method.getAnnotation(IgnoreAuth.class);
        NoIgnoreAuth noIgnoreAuth = method.getAnnotation(NoIgnoreAuth.class);


        return beanIgnoreAuth != null
                ? noIgnoreAuth == null
                : ignoreAuth != null;
    }


}
