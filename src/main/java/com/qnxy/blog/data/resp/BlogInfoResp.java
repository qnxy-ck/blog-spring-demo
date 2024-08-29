package com.qnxy.blog.data.resp;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Qnxy
 */
@Data
public class BlogInfoResp {

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 所属用户id
     */
    private Long userId;

    /**
     * 博客作者名称
     */
    private String username;

    /**
     * 点赞数量
     */
    private Long starsCount;

    /**
     * 收藏数量
     */
    private Long collectionsCount;

    /**
     * 阅读数量
     */
    private Long readsCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 该博客的标签
     */
    private List<BlogTagResp> tagList;


}
