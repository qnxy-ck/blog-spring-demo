package com.qnxy.blog.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.qnxy.blog.configuration.auth.AuthenticationConfigurationProperties;
import com.qnxy.blog.configuration.auth.CurrentAuthUserIdHandlerMethodArgumentResolver;
import com.qnxy.blog.configuration.auth.JwtAuthHandlerInterceptor;
import com.qnxy.blog.core.json.LocalDateTimeJsonSerializer;
import com.qnxy.blog.core.json.LongValueJsonSerializer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Qnxy
 */
@SuppressWarnings("NullableProblems")
@Configuration
@RequiredArgsConstructor
public class ProjectConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                return Optional.ofNullable(request.getHeader("Accept-Language"))
                        .filter(StringUtils::hasText)
                        .map(StringUtils::parseLocale)
                        .orElseGet(() -> new Locale("zh", "CN"));
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
            }
        };
    }


    /**
     * jwt 签名算法
     */
    @Bean
    public Algorithm jwtSignAlgorithm(AuthenticationConfigurationProperties authenticationConfigurationProperties) {
        return Algorithm.HMAC256(authenticationConfigurationProperties.getJwtSecret());
    }

    /**
     * jwt 信息验证器
     *
     * @param jwtSignAlgorithm 验证算法
     */
    @Bean
    public JWTVerifier jwtVerifier(Algorithm jwtSignAlgorithm) {
        return JWT.require(jwtSignAlgorithm).build();
    }


    /**
     * Jackson 增加自定义配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return it -> it.serializers(
                new LongValueJsonSerializer(),
                new LocalDateTimeJsonSerializer()
        );
    }

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
