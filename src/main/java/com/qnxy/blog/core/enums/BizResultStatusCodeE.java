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

    /**
     * 账户名已存在
     */
    ACCOUNT_NAME_ALREADY_EXISTS("B00000"),

    /**
     * 上传文件名称不能为空
     */
    UPLOAD_FILE_NAME_CANNOT_BE_EMPTY("B00001"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED("B00002"),

    /**
     * 文件访问失败
     */
    FILE_ACCESS_FAILED("B00003"),

    /**
     * 不存在的文件
     */
    NON_EXISTENT_FILE("B00004"),

    /**
     * 该分组名称已存在
     */
    GROUP_NAME_ALREADY_EXISTS("B00005"),

    /**
     * 默认分组无法修改
     */
    DEFAULT_GROUPING_CANNOT_BE_MODIFIED("B00006"),

    /**
     * 不存在该分组信息
     */
    GROUP_INFORMATION_DOES_NOT_EXIST("B00007"),
    
    


    ;
    private final String code;
}
