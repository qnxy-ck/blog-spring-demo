package com.qnxy.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.resp.UserFollowerInfoResp;
import com.qnxy.blog.mapper.FollowInfoMapper;
import com.qnxy.blog.service.FollowInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.qnxy.blog.core.VerificationExpectations.expectDeleteOk;
import static com.qnxy.blog.core.VerificationExpectations.expectInsertOk;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class FollowInformationServiceImpl implements FollowInformationService {

    private final FollowInfoMapper followInfoMapper;

    @Override
    public PageInfo<UserFollowerInfoResp> followList(PageReq<Long> pageReq) {
        return pageReq.queryPageInfo(this.followInfoMapper::selectFollowListByUserId);
    }

    @Override
    public void unfollow(Long userId, Long followId) {
        expectDeleteOk(this.followInfoMapper.deleteFollowInfo(userId, followId));
    }

    @Override
    public void follow(Long userId, Long followId) {
        expectInsertOk(this.followInfoMapper.insertFollowInfo(userId, followId));
    }
}
