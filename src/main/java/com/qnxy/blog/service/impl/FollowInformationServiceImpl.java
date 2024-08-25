package com.qnxy.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.resp.UserFollowerInfo;
import com.qnxy.blog.mapper.FollowInfoMapper;
import com.qnxy.blog.service.FollowInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class FollowInformationServiceImpl implements FollowInformationService {

    private final FollowInfoMapper followInfoMapper;

    @Override
    public PageInfo<UserFollowerInfo> followList(Long userId, PageReq<Void> pageReq) {
        return pageReq.withData(userId, pageReq)
                .queryPageInfo(this.followInfoMapper::selectFollowListByUserId);
    }
}
