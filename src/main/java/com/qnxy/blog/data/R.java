package com.qnxy.blog.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qnxy.blog.core.ResultStatusCode;
import com.qnxy.blog.core.ex.BizException;
import lombok.Data;

import static com.qnxy.blog.data.enums.CommonResultStatusCodeE.SUCCESSFUL;

/**
 * 全局统一返回结果
 *
 * @author Qnxy
 */
@Data
public class R<DATA> {
    /**
     * 返回实际数据
     */
    private final DATA data;

    /**
     * 返回状态码
     */
    private final String statusCode;

    /**
     * 返回状态信息
     */
    private final String statusMsg;

    /**
     * 返回时时间戳
     */
    private final long timestamp = System.currentTimeMillis();

    /**
     * 服务器未知错误信息, 方便排查错误
     * 将错误异常栈信息返回
     * <p>
     * 需要开启 发送异常栈信息开关
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String exStackTrace;


    private R(DATA data, String statusCode, String statusMsg, String exStackTrace) {
        this.data = data;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.exStackTrace = exStackTrace;
    }

    public static <DATA, STATUS extends Enum<STATUS> & ResultStatusCode> R<DATA> result(DATA data, STATUS status, Object... args) {
        return new R<>(data, status.getCode(), status.getFullMessage(args), null);
    }

    public static <DATA> R<DATA> ofBizEx(BizException e, String exStackTrace) {
        return new R<>(null, e.getStatus().getCode(), e.getMessage(), exStackTrace);
    }

    public static <DATA, STATUS extends Enum<STATUS> & ResultStatusCode> R<DATA> exStackTrace(STATUS status, String exStackTrace, Object... args) {
        return new R<>(null, status.getCode(), status.getFullMessage(args), exStackTrace);
    }

    public static <DATA> R<DATA> suc(DATA data) {
        return new R<>(data, SUCCESSFUL.getCode(), SUCCESSFUL.getMessage(), null);
    }

    public static <DATA, STATUS extends Enum<STATUS> & ResultStatusCode> R<DATA> fail(STATUS status, Object... args) {
        return new R<>(null, status.getCode(), status.getFullMessage(args), null);
    }


}
