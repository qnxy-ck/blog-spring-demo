package com.qnxy.blog.core.annotations;

import com.qnxy.blog.core.CiphertextHandler;
import com.qnxy.blog.core.ciphertext.AesEcbPKCS5PaddingCiphertextHandler;

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
    Class<? extends CiphertextHandler> value() default AesEcbPKCS5PaddingCiphertextHandler.class;

}
