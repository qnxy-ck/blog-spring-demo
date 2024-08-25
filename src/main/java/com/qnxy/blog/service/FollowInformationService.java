package com.qnxy.blog.service;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.resp.UserFollowerInfoResp;

/**
 * @author Qnxy
 */
public interface FollowInformationService {

    /**
     * 查询用户关注用户列表
     *
     * @param userId  当前用户id
     * @param pageReq 分页查询信息
     * @return 关注列表+分页信息
     */
    PageInfo<UserFollowerInfoResp> followList(Long userId, PageReq<Void> pageReq);

    /**
     * 取消关注
     *
     * @param userId   用户id
     * @param followId 取消关注谁
     */
    void unfollow(Long userId, Long followId);

    /**
     * 增加关注
     *
     * @param userId   用户id
     * @param followId 关注谁
     */
    void follow(Long userId, Long followId);

}
