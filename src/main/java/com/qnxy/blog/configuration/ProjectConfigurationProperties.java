package com.qnxy.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目属性配置信息
 * 
 * @author Qnxy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "blog.config")
public class ProjectConfigurationProperties {

    /**
     * 文件服务器地址
     */
    private String fileUploadServerBaseUrl;

}
