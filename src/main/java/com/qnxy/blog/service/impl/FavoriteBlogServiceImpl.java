package com.qnxy.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.core.enums.BizResultStatusCodeE;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import com.qnxy.blog.mapper.FavoriteBlogGroupMapper;
import com.qnxy.blog.service.FavoriteBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.qnxy.blog.core.VerificationExpectations.expectFalse;
import static com.qnxy.blog.core.VerificationExpectations.expectInsertOk;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class FavoriteBlogServiceImpl implements FavoriteBlogService {

    private final FavoriteBlogGroupMapper favoriteBlogGroupMapper;

    @Override
    public void addFavoriteBlogGroup(Long userId, FavoriteBolgGroupReq favoriteBolgGroupReq) {

        // 如果该用户组名已存在则不添加
        expectFalse(this.favoriteBlogGroupMapper.selectUserIdAndNameExists(userId, favoriteBolgGroupReq.getName()), BizResultStatusCodeE.GROUP_NAME_ALREADY_EXISTS);

        expectInsertOk(this.favoriteBlogGroupMapper.insertFavoriteBlogGroup(userId, favoriteBolgGroupReq));
    }

    @Override
    public PageInfo<FavoriteBlogGroupResp> findGroupList(PageReq<Long> pageReq) {
        return pageReq.queryPageInfo(this.favoriteBlogGroupMapper::selectGroupListByUserId);
    }


}
