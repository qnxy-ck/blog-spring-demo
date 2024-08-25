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

    ACCOUNT_NAME_ALREADY_EXISTS("B00000"),
    UPLOAD_FILE_NAME_CANNOT_BE_EMPTY("B00001"),
    FILE_UPLOAD_FAILED("B00002"),
    FILE_ACCESS_FAILED("B00003"),
    NON_EXISTENT_FILE("B00004"),


    ;
    private final String code;
}
