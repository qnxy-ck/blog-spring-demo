package com.qnxy.blog.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关注信息
 *
 * @author Qnxy
 */
@Data
public class FollowInfo {

    private Long id;

    /**
     * 谁关注的
     */
    private Long userId;

    /**
     * 关注的谁
     */
    private Long followId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;

}
