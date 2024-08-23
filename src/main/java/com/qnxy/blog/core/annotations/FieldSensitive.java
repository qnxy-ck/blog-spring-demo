package com.qnxy.blog.core.annotations;

import com.qnxy.blog.core.CiphertextHandler;
import com.qnxy.blog.core.ciphertext.AesEcbPKCS5PaddingCiphertextHandler;

import java.lang.annotation.*;

/**
 * 敏感数据加解密
 * <p>
 * 可指定加解密的方式, 指定 {@link CiphertextHandler} 接口的实现类即可
 * 默认加密实现类为 {@link AesEcbPKCS5PaddingCiphertextHandler}
 *
 * @author Qnxy
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSensitive {

    /**
     * 指定该字段加密的方式
     */
    Class<? extends CiphertextHandler> value() default AesEcbPKCS5PaddingCiphertextHandler.class;

}
