package com.qnxy.blog.data.resp;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Qnxy
 */
@Data
public class BlogTagResp {

    /**
     * 标签id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    private LocalDateTime createTime;
}
