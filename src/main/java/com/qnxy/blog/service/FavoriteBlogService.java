package com.qnxy.blog.service;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;

/**
 * @author Qnxy
 */
public interface FavoriteBlogService {

    String FAVORITE_BLOG_DEFAULT_GROUP_NAME = "默认分组";

    void addFavoriteBlogGroup(Long userId, FavoriteBolgGroupReq favoriteBolgGroupReq);

    /**
     * 查询博客收藏分组列表
     *
     * @param pageReq 分页参数+用户id
     * @return .
     */
    PageInfo<FavoriteBlogGroupResp> findGroupList(PageReq<Long> pageReq);

    /**
     * 修改收藏分组信息
     *
     * @param userId               当前用户ID
     * @param favoriteBolgGroupReq 待修改的信息
     */
    void updateGroupInfo(Long userId, FavoriteBolgGroupReq favoriteBolgGroupReq);

    /**
     * 删除分组信息
     * 无法删除默认分组
     * <p>
     * 如果该分组存在收藏数据, 将移动到默认分组中
     *
     * @param id     待修改分组ID
     * @param userId 当前用户ID
     */
    void deleteGroup(Long id, Long userId);
}
