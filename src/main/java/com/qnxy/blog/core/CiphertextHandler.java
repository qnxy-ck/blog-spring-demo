package com.qnxy.blog.core;

/**
 * 密文处理统一接口
 *
 * @author Qnxy
 */
public interface CiphertextHandler {

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
