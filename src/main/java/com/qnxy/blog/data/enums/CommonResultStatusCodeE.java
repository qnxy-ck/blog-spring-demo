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

    SUCCESSFUL("C00001", "成功"),
    FAILED("C00002", "失败"),
    UNKNOWN_EXCEPTION("C00003", "系统未知错误"),
    JSON_CONVERSION_EXCEPTION("C00004", "Json转换异常"),
    UNAUTHORIZED_ACCESS("C00005", "未授权的访问: {0}"),
    INCORRECT_ACCOUNT_OR_PASSWORD("C00006", "账号或密码错误"),
    SIGNATURE_VERIFICATION_EXCEPTION("C00007", "签名验证异常"),
    FILE_UPLOAD_FAILED("C00008", "文件上传失败"),


    ;
    private final String code;
    private final String message;

}
