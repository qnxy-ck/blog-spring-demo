package com.qnxy.blog.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客标签
 *
 * @author Qnxy
 */
@Data
public class BlogTags {

    private Long id;

    /**
     * 该标签所属用户
     */
    private Long userId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

}
