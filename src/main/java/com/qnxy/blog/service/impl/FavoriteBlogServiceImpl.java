package com.qnxy.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.qnxy.blog.data.PageReq;
import com.qnxy.blog.data.entity.FavoriteBlogGroup;
import com.qnxy.blog.data.event.UserRegisterEvent;
import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import com.qnxy.blog.mapper.FavoriteBlogGroupMapper;
import com.qnxy.blog.mapper.FavoriteBlogMapper;
import com.qnxy.blog.service.FavoriteBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;
import java.util.stream.Stream;

import static com.qnxy.blog.core.CommonResultStatusCodeE.DATA_TO_BE_MODIFIED_DOES_NOT_EXIST;
import static com.qnxy.blog.core.VerificationExpectations.*;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.DEFAULT_GROUPING_CANNOT_BE_MODIFIED;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.GROUP_NAME_ALREADY_EXISTS;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteBlogServiceImpl implements FavoriteBlogService {

    private final FavoriteBlogGroupMapper favoriteBlogGroupMapper;
    private final FavoriteBlogMapper favoriteBlogMapper;

    @Override
    public void addFavoriteBlogGroup(Long userId, FavoriteBolgGroupReq favoriteBolgGroupReq) {

        // 如果该用户组名已存在则不添加
        expectFalse(this.favoriteBlogGroupMapper.selectUserIdAndNameExists(userId, favoriteBolgGroupReq.getName()), GROUP_NAME_ALREADY_EXISTS);

        expectInsertOk(this.favoriteBlogGroupMapper.insertFavoriteBlogGroup(userId, favoriteBolgGroupReq));
    }

    @Override
    public PageInfo<FavoriteBlogGroupResp> findGroupList(PageReq<Long> pageReq) {
        return pageReq.queryPageInfo(this.favoriteBlogGroupMapper::selectGroupListByUserId);
    }

    @Override
    public void updateGroupInfo(Long userId, FavoriteBolgGroupReq favoriteBolgGroupReq) {
        final FavoriteBlogGroup favoriteBlogGroup = expectNonNull(this.favoriteBlogGroupMapper.selectGroupById(favoriteBolgGroupReq.getGroupId()), DATA_TO_BE_MODIFIED_DOES_NOT_EXIST);
        // 检查是否为默认分组. 默认分组无法修改名称
        expectFalse(favoriteBlogGroup.getDefaultGroup() && favoriteBlogGroup.getName() == null, DEFAULT_GROUPING_CANNOT_BE_MODIFIED);

        if (Stream.of(favoriteBolgGroupReq.getName(),
                        favoriteBolgGroupReq.getCover(),
                        favoriteBolgGroupReq.getOpen(),
                        favoriteBolgGroupReq.getDescription()
                )
                .allMatch(Objects::nonNull)) {
            // 无需修改
            return;
        }

        expectUpdateOk(this.favoriteBlogGroupMapper.updateGroupInfo(userId, favoriteBolgGroupReq));
    }

    /**
     * 删除该分组下收藏的博客信息
     * <p>
     * 修改思路
     * 查询当前分组是否存在, 如果不存在则不修改, 如果存在判断是否为默认分组(默认分组无法删除)
     * 查询默认分组ID, 将当前分组下的数据移动到默认分组下(修改当前分组ID为默认分组ID即可)
     * <p>
     * 修改完成后 默认分组下的数据(博客ID)可能和默认分组中的出现重复
     * 这里保留两个分组中最早收藏的博客数据
     *
     * @param id     待修改分组ID
     * @param userId 当前用户ID
     */
    @Override
    @Transactional
    public void deleteGroup(Long id, Long userId) {
        // 判断待删除的数据是否为空, 为空则报错, 后续需要使用该数据
        final FavoriteBlogGroup favoriteBlogGroup = expectNonNull(this.favoriteBlogGroupMapper.selectGroupById(id), DATA_TO_BE_MODIFIED_DOES_NOT_EXIST);
        // 默认分组无法删除
        expectFalse(favoriteBlogGroup.getDefaultGroup(), DEFAULT_GROUPING_CANNOT_BE_MODIFIED);
        // 查询默认分组信息
        final FavoriteBlogGroup defaultGroup = this.favoriteBlogGroupMapper.selectDefaultGroupByUserId(userId);

        // 将待删除分组ID修改为默认分组ID
        this.favoriteBlogMapper.updateGroupIdByUserId(favoriteBlogGroup.getId(), defaultGroup.getId());
        // 删除该分组下重复的博客ID
        this.favoriteBlogMapper.deleteDuplicateBlogIdByGroupId(defaultGroup.getId());

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
