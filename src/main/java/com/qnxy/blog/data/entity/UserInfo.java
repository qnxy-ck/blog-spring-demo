package com.qnxy.blog.data.entity;

import com.qnxy.blog.data.GenderType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
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
     * 用户头像
     */
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
