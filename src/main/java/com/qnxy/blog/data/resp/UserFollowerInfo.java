package com.qnxy.blog.data.resp;

import com.qnxy.blog.core.annotations.ResourceAccess;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关注列表信息
 *
 * @author Qnxy
 */
@Data
public class UserFollowerInfo {

    /**
     * 被关注者ID
     */
    private Long followerId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    @ResourceAccess
    private String userAvatar;

    /**
     * 职业
     */
    private String profession;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;

}
