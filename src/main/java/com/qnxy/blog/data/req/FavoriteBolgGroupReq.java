package com.qnxy.blog.data.req;

import com.qnxy.blog.core.annotations.MustNotBeBlankWhenNotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * 收藏分组请求参数
 *
 * @author Qnxy
 */
@Data
public class FavoriteBolgGroupReq {

    @NotNull(message = "分组ID不能为空", groups = Update.class)
    private Long groupId;

    /**
     * 分组封面
     */
    @MustNotBeBlankWhenNotNull(message = "封面不能为空字符", groups = Update.class)
    private String cover;

    /**
     * 组名
     */
    @NotBlank(message = "收藏分组名称不能为空", groups = Insert.class)
    @MustNotBeBlankWhenNotNull(message = "收藏分组名称不能为空", groups = Update.class)
    private String name;

    /**
     * 是否为公开的
     * 公开为 true
     */
    @NotNull(message = "没有选择是否公开", groups = Insert.class)
    private Boolean open;

    /**
     * 分组描述
     */
    @MustNotBeBlankWhenNotNull(message = "描述不能为空字符", groups = Update.class)
    private String description;

}
