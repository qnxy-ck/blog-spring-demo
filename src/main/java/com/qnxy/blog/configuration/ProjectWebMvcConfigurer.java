package com.qnxy.blog.configuration;

import com.qnxy.blog.configuration.auth.CurrentAuthUserIdHandlerMethodArgumentResolver;
import com.qnxy.blog.configuration.auth.JwtAuthHandlerInterceptor;
import com.qnxy.blog.core.ApiVersionControlRequestMappingHandlerMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * WebMvcConfigurer 配置
 *
 * @author Qnxy
 */
@Component
@RequiredArgsConstructor
public class ProjectWebMvcConfigurer implements WebMvcConfigurer, WebMvcRegistrations {

    private final JwtAuthHandlerInterceptor jwtAuthHandlerInterceptor;
    private final CurrentAuthUserIdHandlerMethodArgumentResolver currentAuthUserIdHandlerMethodArgumentResolver;
    private final ProjectConfigurationProperties projectConfigurationProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthHandlerInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentAuthUserIdHandlerMethodArgumentResolver);
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionControlRequestMappingHandlerMapping(projectConfigurationProperties.getVersionControlHigh());
    }
}
