package com.qnxy.blog.core.ciphertext;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import com.qnxy.blog.core.CiphertextHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 要求配置 {@link ProjectConfigurationProperties#getSensitiveSm4Key()} 必须配置且不能为空
 * <p>
 * 加密方式AES/ECB/PKCS5Padding
 *
 * @author Qnxy
 */
@Component
public class Sm4EcbPKCS5PaddingCiphertextHandler implements CiphertextHandler {

    private final SM4 sm4;

    public Sm4EcbPKCS5PaddingCiphertextHandler(ProjectConfigurationProperties projectConfigurationProperties) {
        final String sensitiveSm4Key = projectConfigurationProperties.getSensitiveSm4Key();
        if (StringUtils.hasText(sensitiveSm4Key)) {
            this.sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding, SecureUtil.decode(sensitiveSm4Key));
        } else {
            throw new IllegalArgumentException("未设置 SM4 密钥");
        }
    }


    @Override
    public String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }

        return sm4.encryptBase64(plainText);
    }

    @Override
    public String decrypt(String ciphertext) {
        if (ciphertext == null) {
            return null;
        }
        return sm4.decryptStr(ciphertext);
    }


}
