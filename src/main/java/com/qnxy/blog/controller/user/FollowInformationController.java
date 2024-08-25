package com.qnxy.blog.controller.user;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.resp.UserFollowerInfo;
import com.qnxy.blog.service.FollowInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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
    public PageInfo<UserFollowerInfo> followList(CurrentAuthUserId authUserId, @RequestBody PageReq<Void> pageReq) {
        return this.followInformationService.followList(authUserId.getUserId(), pageReq);
    }

}
