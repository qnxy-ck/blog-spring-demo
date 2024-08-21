package com.qnxy.blog.data.entity;

import com.qnxy.blog.core.annotations.Desensitization;
import com.qnxy.blog.core.annotations.ResourceAccess;
import com.qnxy.blog.core.annotations.Sensitive;
import com.qnxy.blog.data.GenderType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author Qnxy
 */
@Data
public class UserInfo {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户手机号
     */
    @Sensitive
    @Desensitization(Desensitization.ValueType.PHONE_NUMBER)
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
