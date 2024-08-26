package com.qnxy.blog.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏列表分组信息
 *
 * @author Qnxy
 */
@Data
public class FavoriteBlogGroup {

    private Long id;

    /**
     * 该收藏组属于哪个用户
     */
    private Long userId;

    /**
     * 分组封面
     */
    private String cover;

    /**
     * 组名
     */
    private String name;

    /**
     * 是否为公开的
     * 公开为 true
     */
    private Boolean open;

    /**
     * 是否为默认分组
     */
    private Boolean defaultGroup;

    /**
     * 分组描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
