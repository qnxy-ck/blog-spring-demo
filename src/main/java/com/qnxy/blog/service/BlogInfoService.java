package com.qnxy.blog.service;

import com.qnxy.blog.data.req.AddBlogReq;
import com.qnxy.blog.data.resp.BlogInfoResp;

/**
 * @author Qnxy
 */
public interface BlogInfoService {

    /**
     * 添加博客
     *
     * @param addBlogReq 待博客信息
     * @param userId     当前用户id
     */
    void addBlog(AddBlogReq addBlogReq, Long userId);

    /**
     * 对某个博客点赞
     *
     * @param blogId 点赞那个博客
     * @param userId 谁点的赞
     */
    void starsBlog(String blogId, Long userId);

    /**
     * 查看博客信息
     *
     * @param blogId 差点那个博客
     * @return .
     */
    BlogInfoResp readBlog(Long blogId);

}
