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

}
