package com.qnxy.blog.core.ex;

import com.qnxy.blog.core.ResultStatusCode;

/**
 * @author Qnxy
 */
public class BizException extends RuntimeException {

    private final Object _status;

    public <STATUS extends Enum<STATUS> & ResultStatusCode> BizException(STATUS status, Object... args) {
        super(status.getFullMessage(args));
        this._status = status;
    }

    public <STATUS extends Enum<STATUS> & ResultStatusCode> STATUS getStatus() {
        //noinspection unchecked
        return (STATUS) _status;
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
