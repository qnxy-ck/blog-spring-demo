package com.qnxy.blog.core.enums;

import com.qnxy.blog.core.ResultStatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 业务状态信息定义
 *
 * @author Qnxy
 */
@RequiredArgsConstructor
@Getter
public enum BizResultStatusCodeE implements ResultStatusCode {

    ACCOUNT_NAME_ALREADY_EXISTS("B00000", "账户名已存在"),


    ;
    private final String code;
    private final String message;
}
