package com.qnxy.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.core.enums.BizResultStatusCodeE;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.event.UserRegisterEvent;
import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import com.qnxy.blog.mapper.FavoriteBlogGroupMapper;
import com.qnxy.blog.service.FavoriteBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.qnxy.blog.core.VerificationExpectations.expectFalse;
import static com.qnxy.blog.core.VerificationExpectations.expectInsertOk;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
@Slf4j
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

    /**
     * 监听用户创建事件, 创建完成后执行该方法
     * 在用户创建事件提交后执行
     *
     * @param userRegisterEvent 事件信息
     */
    @TransactionalEventListener
    public void createDefaultFavoriteBlogGroup(UserRegisterEvent userRegisterEvent) {

        boolean flag = this.favoriteBlogGroupMapper.insertDefaultFavoriteBlogGroup(userRegisterEvent.getUserId(), FAVORITE_BLOG_DEFAULT_GROUP_NAME);

        if (!flag) {
            log.error("默认分组创建失败: {}", userRegisterEvent.getUserId());
        }
        
    }


}
