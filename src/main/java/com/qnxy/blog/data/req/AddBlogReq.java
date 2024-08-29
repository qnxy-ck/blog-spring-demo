package com.qnxy.blog.data.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author Qnxy
 */
@Data
public class AddBlogReq {

    /**
     * 博客标题
     */
    @NotBlank(message = "未指定标题")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 指定的标签id
     */
    private List<Long> tagIdList;


}
