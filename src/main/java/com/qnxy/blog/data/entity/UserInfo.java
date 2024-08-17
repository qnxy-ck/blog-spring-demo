package com.qnxy.blog.data.entity;

import lombok.Data;

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
