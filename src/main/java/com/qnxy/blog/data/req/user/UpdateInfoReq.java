package com.qnxy.blog.data.req.user;

import com.qnxy.blog.core.annotations.MustNotBeBlankWhenNotNull;
import com.qnxy.blog.data.GenderType;
import lombok.Data;

/**
 * 个人信息修改
 *
 * @author Qnxy
 */
@Data
public class UpdateInfoReq {

    /**
     * 用户头像
     */
    @MustNotBeBlankWhenNotNull(message = "头像地址不能为空")
    private String userAvatar;

    /**
     * 性别
     */
    private GenderType gender;

    /**
     * 职业
     */
    @MustNotBeBlankWhenNotNull(message = "职业信息不能为空")
    private String profession;

    /**
     * 个人描述
     */
    private String personalDescription;

}
