package com.qnxy.blog.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.ResultStatusCode;
import lombok.Data;

import static com.qnxy.blog.core.CommonResultStatusCodeE.SUCCESSFUL;

/**
 * 全局统一返回结果
 *
 * @author Qnxy
 */
@Data
public final class R<DATA> {
    /**
     * 返回实际数据
     */
    private final DATA data;

    /**
     * 返回状态码
     */
    private final String statusCode;

    @JsonIgnore
    private Object[] args;


    private R(DATA data, String statusCode, Object... args) {
        this.data = data;
        this.statusCode = statusCode;
        this.args = args;
    }

    public static <DATA> R<DATA> suc(DATA data) {
        return new R<>(data, SUCCESSFUL.getCode());
    }

    public static <DATA, STATUS extends Enum<STATUS> & ResultStatusCode> R<DATA> fail(STATUS status, Object... args) {
        return new R<>(null, status.getCode(), args);
    }

    public static <DATA, STATUS extends Enum<STATUS> & ResultStatusCode> R<DATA> result(DATA data, STATUS status, Object... args) {
        return new R<>(data, status.getCode(), args);
    }

    public static <DATA> R<DATA> ofBizEx(BizException e) {
        return new R<>(null, e.getCode(), e.getArgs());
    }


}
