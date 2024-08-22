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
    UPLOAD_FILE_NAME_CANNOT_BE_EMPTY("B00001", "上传文件名称不能为空"),
    FILE_UPLOAD_FAILED("B00002", "文件上传失败"),
    FILE_ACCESS_FAILED("B00003", "文件访问失败"),
    NON_EXISTENT_FILE("B00004", "不存在的文件"),


    ;
    private final String code;
    private final String message;
}
