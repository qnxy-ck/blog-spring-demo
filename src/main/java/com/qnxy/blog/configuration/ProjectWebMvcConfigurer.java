package com.qnxy.blog.configuration;

import com.qnxy.blog.configuration.auth.CurrentAuthUserIdHandlerMethodArgumentResolver;
import com.qnxy.blog.configuration.auth.JwtAuthHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvcConfigurer 配置
 *
 * @author Qnxy
 */
@Component
@RequiredArgsConstructor
public class ProjectWebMvcConfigurer implements WebMvcConfigurer {

    private final JwtAuthHandlerInterceptor jwtAuthHandlerInterceptor;
    private final CurrentAuthUserIdHandlerMethodArgumentResolver currentAuthUserIdHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthHandlerInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentAuthUserIdHandlerMethodArgumentResolver);
    }

}
