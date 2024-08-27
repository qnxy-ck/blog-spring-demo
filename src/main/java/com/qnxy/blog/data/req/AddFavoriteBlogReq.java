package com.qnxy.blog.data.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;

/**
 * @author Qnxy
 */
@Data
public class AddFavoriteBlogReq {

    /**
     * 收藏那个博客
     */
    @NotNull(message = "博客ID不能为空", groups = {Insert.class})
    private Long blogId;

    /**
     * 收藏到那个分组
     * 如果为空则使用默认分组
     */
    private Long groupId;


}
