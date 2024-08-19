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
    CryptoType cryptoType() default CryptoType.AES;

    enum CryptoType {
        SM4,
        AES,
    }


}
