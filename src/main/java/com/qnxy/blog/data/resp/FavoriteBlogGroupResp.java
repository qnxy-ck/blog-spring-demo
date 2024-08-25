package com.qnxy.blog.data.resp;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Qnxy
 */
@Data
public class FavoriteBlogGroupResp {

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
     * 分组描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 该组下面有多少博客数量
     */
    private Integer blogCount;

}
