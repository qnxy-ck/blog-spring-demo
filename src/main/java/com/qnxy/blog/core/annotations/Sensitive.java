package com.qnxy.blog.core.annotations;

import java.lang.annotation.*;

/**
 * 敏感数据加解密
 *
 * @author Qnxy
 */
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 指定该字段加密的方式
     * 默认为 AES 加密
     */
    EncryptType value() default EncryptType.AES;


    /**
     * 支持的加密类型
     */
    enum EncryptType {
        SM4,
        AES,
    }


    /**
     * 加解密处理器
     * 标注在密文处理器上 {@link CiphertextHandler}, 并指定该处理器处理的加密方式
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Handler {

        /**
         * 当前加解密处理器
         */
        EncryptType value();

    }

    /**
     * 加解密处理接口
     */
    interface CiphertextHandler {

        /**
         * 加密
         *
         * @param plainText 待加密数据
         * @return 密文
         */
        String encrypt(String plainText);

        /**
         * 解密
         *
         * @param ciphertext 密文
         * @return 解密后的数据
         */
        String decrypt(String ciphertext);

    }

}
