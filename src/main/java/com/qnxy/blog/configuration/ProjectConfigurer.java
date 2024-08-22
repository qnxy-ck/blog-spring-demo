package com.qnxy.blog.configuration;

import com.qnxy.blog.configuration.auth.CurrentAuthUserIdHandlerMethodArgumentResolver;
import com.qnxy.blog.configuration.auth.JwtAuthHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@Configuration
public class ProjectConfigurer {

    /**
     * WebMvcConfigurer 配置
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(
            JwtAuthHandlerInterceptor jwtAuthHandlerInterceptor,
            CurrentAuthUserIdHandlerMethodArgumentResolver currentAuthUserIdHandlerMethodArgumentResolver
    ) {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(jwtAuthHandlerInterceptor)
                        .addPathPatterns("/**");
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(currentAuthUserIdHandlerMethodArgumentResolver);
            }
        };
    }
}
