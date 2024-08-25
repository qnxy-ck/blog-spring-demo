package com.qnxy.blog.core;

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


    default BizException createEx(Object... args) {
        return BizException.create(this, args);
    }

    default Supplier<BizException> createSupplierEx(Object... args) {
        return () -> BizException.create(this, args);
    }

}
