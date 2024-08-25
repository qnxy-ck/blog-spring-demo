package com.qnxy.blog.controller.user;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.resp.UserFollowerInfoResp;
import com.qnxy.blog.service.FollowInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关注信息
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowInformationController {

    private final FollowInformationService followInformationService;

    /**
     * 查询用户关注用户列表
     *
     * @param authUserId 当前用户id
     * @param pageReq    分页查询信息
     * @return 关注列表+分页信息
     */
    @PostMapping("/list")
    public PageInfo<UserFollowerInfoResp> followList(CurrentAuthUserId authUserId, @RequestBody PageReq<Void> pageReq) {
        return this.followInformationService.followList(pageReq.withData(authUserId.getUserId()));
    }

    /**
     * 取消关注
     *
     * @param authUserId 用户id
     * @param followId   取消关注谁
     */
    @DeleteMapping("/{followId}")
    public void unfollow(CurrentAuthUserId authUserId, @PathVariable Long followId) {
        this.followInformationService.unfollow(authUserId.getUserId(), followId);
    }

    /**
     * 增加关注
     *
     * @param authUserId 用户id
     * @param followId   关注谁
     */
    @PostMapping("/{followId}")
    public void follow(CurrentAuthUserId authUserId, @PathVariable Long followId) {
        this.followInformationService.follow(authUserId.getUserId(), followId);
    }


}
