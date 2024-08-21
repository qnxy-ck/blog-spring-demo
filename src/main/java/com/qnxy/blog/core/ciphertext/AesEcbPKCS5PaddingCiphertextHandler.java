package com.qnxy.blog.core.ciphertext;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import com.qnxy.blog.core.CiphertextHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 要求配置 {@link ProjectConfigurationProperties#getSensitiveAesKey()} 必须配置且不能为空
 * <p>
 * 加密方式 AES/ECB/PKCS5Padding
 *
 * @author Qnxy
 */
@Component
public class AesEcbPKCS5PaddingCiphertextHandler implements CiphertextHandler {

    private final AES aes;

    public AesEcbPKCS5PaddingCiphertextHandler(ProjectConfigurationProperties projectConfigurationProperties) {
        final String sensitiveAesKey = projectConfigurationProperties.getSensitiveAesKey();
        if (StringUtils.hasText(sensitiveAesKey)) {
            this.aes = SecureUtil.aes(sensitiveAesKey.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new IllegalArgumentException("未设置 AES 密钥");
        }
    }

    @Override
    public String encrypt(String plainText) {
        if (plainText != null) {
            return aes.encryptBase64(plainText);
        }

        return null;
    }

    @Override
    public String decrypt(String ciphertext) {
        if (ciphertext != null) {
            return aes.decryptStr(ciphertext);
        }
        return null;
    }


}
