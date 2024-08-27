package com.qnxy.blog.core;

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

    /**
     * 成功
     */
    SUCCESSFUL("C00001"),

    /**
     * 失败
     */
    FAILED("C00002"),

    /**
     * 系统未知错误
     */
    UNKNOWN_EXCEPTION("C00003"),

    /**
     * Json转换异常
     */
    JSON_CONVERSION_EXCEPTION("C00004"),

    /**
     * 未授权的访问
     */
    UNAUTHORIZED_ACCESS("C00005"),

    /**
     * 账号或密码错误
     */
    INCORRECT_ACCOUNT_OR_PASSWORD("C00006"),

    /**
     * 签名验证异常
     */
    SIGNATURE_VERIFICATION_EXCEPTION("C00007"),

    /**
     * 参数校验不通过
     */
    PARAMETER_VERIFICATION_FAILED("C00008"),

    /**
     * 数据添加失败
     */
    DATA_ADDITION_FAILED("C00009"),

    /**
     * 数据更新失败
     */
    DATA_UPDATE_FAILED("C00010"),

    /**
     * 数据删除失败
     */
    DATA_DELETION_FAILED("C00011"),

    /**
     * 访问资源不存在: [{0}]
     */
    ACCESS_RESOURCE_DOES_NOT_EXIST("C00012"),

    /**
     * 敏感数据处理失败
     */
    SENSITIVE_DATA_PROCESSING_FAILED("C00013"),

    /**
     * 不存在的性别参数: [{0}] 仅支持: {1}
     */
    GENDER_PARAMETER_DOES_NOT_EXIST("C00014"),

    /**
     * 不支持的请求方式: [{0}]
     */
    UNSUPPORTED_REQUEST_METHOD("C00015"),

    /**
     * 非法的版本格式: [{0}]
     */
    ILLEGAL_VERSION_FORMAT("C00016"),
    
    ;

    private final String code;

}
