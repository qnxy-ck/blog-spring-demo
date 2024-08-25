package com.qnxy.blog.configuration.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Qnxy
 */
@Configuration
public class AuthConfigurer {

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
    
}
