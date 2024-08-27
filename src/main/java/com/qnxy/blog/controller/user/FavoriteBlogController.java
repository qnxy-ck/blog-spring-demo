package com.qnxy.blog.controller.user;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.req.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import com.qnxy.blog.service.FavoriteBlogService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 分组信息修改
     * 默认分组无法修改名称
     *
     * @param authUserId           当前用户ID
     * @param favoriteBolgGroupReq 待修改信息
     */
    @PutMapping("/group")
    public void updateGroupInfo(CurrentAuthUserId authUserId, @RequestBody @Validated(Update.class) FavoriteBolgGroupReq favoriteBolgGroupReq) {
        this.favoriteBlogService.updateGroupInfo(authUserId.getUserId(), favoriteBolgGroupReq);
    }

    /**
     * 删除分组信息
     * 无法删除默认分组
     * <p>
     * 如果该分组存在收藏数据, 将移动到默认分组中
     *
     * @param id         待修改分组ID
     * @param authUserId 当前用户ID
     */
    @DeleteMapping("/group/{id}")
    public void deleteGroup(@PathVariable Long id, CurrentAuthUserId authUserId) {
        this.favoriteBlogService.deleteGroup(id, authUserId.getUserId());
    }


}
