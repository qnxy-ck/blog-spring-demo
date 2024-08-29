package com.qnxy.blog.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章信息
 *
 * @author Qnxy
 */
@Data
public class BlogInfo {

    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 实际内容
     */
    private String content;

    /**
     * 阅读数量
     */
    private Long readsCount;

    /**
     * 收藏数量
     */
    private Long collectionsCount;

    /**
     * 点赞数量
     */
    private Long starsCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}
