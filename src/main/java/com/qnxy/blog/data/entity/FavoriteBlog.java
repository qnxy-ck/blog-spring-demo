package com.qnxy.blog.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏信息
 *
 * @author Qnxy
 */
@Data
public class FavoriteBlog {

    private Long id;

    /**
     * 属于哪个组
     */
    private Long groupId;

    /**
     * 收藏了哪些博客
     */
    private Long blogId;

    /**
     * 添加时间
     */
    private LocalDateTime createdTime;


}
