package com.qnxy.blog.core;

import lombok.Getter;

/**
 * 自定义异常, 用于全局异常拦截, 特殊处理
 *
 * @author Qnxy
 */
@Getter
public class BizException extends RuntimeException {

    private final String code;
    private final Object[] args;

    public <STATUS extends Enum<STATUS> & ResultStatusCode> BizException(Exception e, STATUS status, Object... args) {
        super(e);
        this.code = status.getCode();
        this.args = args;
    }

    public <STATUS extends Enum<STATUS> & ResultStatusCode> BizException(STATUS status, Object... args) {
        this(null, status, args);
    }


    public static <S extends ResultStatusCode, STATUS extends Enum<STATUS> & ResultStatusCode> BizException create(S s, Object... args) {
        if (Enum.class.isAssignableFrom(s.getClass())) {
            //noinspection unchecked
            return new BizException((STATUS) s, args);
        } else {
            throw new UnsupportedOperationException("请使用枚举类实现 ResultStatusCode 接口使用");
        }
    }
}
