package com.qnxy.blog.core;

import com.qnxy.blog.core.ex.BizException;

import java.text.MessageFormat;
import java.util.function.Supplier;

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


    default void throwEx(Object... args) {
        throw BizException.create(this, args);
    }

    default BizException createEx(Object... args) {
        return BizException.create(this, args);
    }

    default Supplier<BizException> createSupplierEx(Object... args) {
        return () -> BizException.create(this, args);
    }

}
