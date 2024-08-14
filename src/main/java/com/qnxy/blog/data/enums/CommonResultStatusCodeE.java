package com.qnxy.blog.data.enums;

import com.qnxy.blog.core.ResultStatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 公共状态信息定义
 *
 * @author Qnxy
 */
@RequiredArgsConstructor
@Getter
public enum CommonResultStatusCodeE implements ResultStatusCode {

    STACK_INFO("-1", "{0}"),
    SUCCESSFUL("C00001", "成功"),
    FAILED("C00002", "失败"),
    UNKNOWN_EXCEPTION("C00003", "系统未知错误"),
    
    Test("C0004", "你好: {0}")


    ;
    private final String code;
    private final String message;

}
