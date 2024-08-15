package com.qnxy.blog.configuration.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author Qnxy
 */
@Data
@Configuration
@ConfigurationProperties("blog.config.auth")
public class AuthenticationConfigurationProperties {

    /**
     * token 请求头名称
     */
    private String requestHeaderTokenName;

    /**
     * jwt 签名秘钥
     */
    private String jwtSecret;

    /**
     * jwt 签发人
     */
    private String jwtIssuer;

    /**
     * jwt 过期时间
     */
    private Duration jwtExpirationTime;

    /**
     * jwt payload key
     */
    private String jwtPayloadKey;
    
    
    private Map<RequestMethod, List<String>> ignoreAuthenticationUrl;

}
