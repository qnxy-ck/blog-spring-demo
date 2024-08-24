package com.qnxy.blog.data.entity;

import com.qnxy.blog.core.annotations.Desensitization;
import com.qnxy.blog.core.annotations.Desensitization.ValueType;
import com.qnxy.blog.core.annotations.FieldSensitive;
import com.qnxy.blog.core.annotations.ResourceAccess;
import com.qnxy.blog.data.GenderType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author Qnxy
 */
@Data
@Accessors(chain = true)
public class UserInfo {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    @Desensitization(ValueType.SET_NULL)
    private String password;

    /**
     * 用户手机号
     */
    @FieldSensitive
    @Desensitization(ValueType.PHONE_NUMBER)
    private String phoneNum;

    /**
     * 用户头像
     */
    @ResourceAccess
    private String userAvatar;

    /**
     * 用户性别
     */
    private GenderType gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 职业
     */
    private String profession;

    /**
     * 个人描述
     */
    private String personalDescription;


    /**
     * 是否删除, 删除为true
     */
    private boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

}
