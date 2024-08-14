package com.qnxy.blog.configuration.ex;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Qnxy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "blog.config")
public class ExceptionConfigurationProperties {

    private Boolean enableExceptionStackTrace;

}
