package com.qnxy.blog.core;

import com.qnxy.blog.core.ex.BizException;

import java.text.MessageFormat;

/**
 * 返回结果状态码接口
 *
 * @author Qnxy
 */
public interface ResultStatusCode {

    /**
     * 状态码
     */
    String getCode();

    /**
     * 状态信息
     */
    String getMessage();


    default String getFullMessage(Object... args) {
        if (args == null || args.length == 0) {
            return getMessage();
        }
        return MessageFormat.format(getMessage(), args);
    }


    default void throwException(Object... args) {
        throw BizException.create(this, args);
    }


}
