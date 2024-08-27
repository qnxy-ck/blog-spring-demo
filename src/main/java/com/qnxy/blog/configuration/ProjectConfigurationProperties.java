package com.qnxy.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

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
     * 当请求接口时如果没有指定版本参数
     * 并且该接口存在多个版本 使用高版本还是默认版本
     * <p>
     * 为 true 则使用最高版本
     * 为 false 则使用低版本
     * <p>
     * 默认使用高版本
     */
    private Boolean versionControlHigh = true;

    /**
     * 文件访问地址前缀
     */
    private String fileAccessAddress;

    /**
     * 文件上传路径
     */
    private Path fileUploadPath;


    /**
     * 敏感数据 AES 加密密钥
     * 秘钥可以为 BASE64 或 HEX 编码类型
     */
    private String sensitiveAesKey;

    /**
     * 敏感数据 SM4 加密密钥
     * 秘钥可以为 BASE64 或 HEX 编码类型
     */
    private String sensitiveSm4Key;

}
