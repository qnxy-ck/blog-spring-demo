package com.qnxy.blog.controller.user;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import com.qnxy.blog.service.FavoriteBlogService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Insert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏的博客/收藏组管理
 *
 * @author Qnxy
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/favoriteBlog")
public class FavoriteBlogController {

    private final FavoriteBlogService favoriteBlogService;

    /**
     * 创建分组
     */
    @PostMapping("/group")
    public void addFavoriteBlogGroup(CurrentAuthUserId authUserId, @RequestBody @Validated(Insert.class) FavoriteBolgGroupReq favoriteBolgGroupReq) {
        this.favoriteBlogService.addFavoriteBlogGroup(authUserId.getUserId(), favoriteBolgGroupReq);
    }

    /**
     * 查询收藏分组信息
     *
     * @param pageReq    分页参数
     * @param authUserId 当前用户id
     * @return 收藏分组数据
     */
    @PostMapping("/group/list")
    public PageInfo<FavoriteBlogGroupResp> groupList(@RequestBody PageReq<Void> pageReq, CurrentAuthUserId authUserId) {
        return this.favoriteBlogService.findGroupList(pageReq.withData(authUserId.getUserId()));
    }


}
