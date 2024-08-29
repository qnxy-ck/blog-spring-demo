package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.FavoriteBlog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Qnxy
 */
@Mapper
public interface FavoriteBlogMapper {


    /**
     * 将分组ID修改为另一个
     *
     * @param groupId       待修改的
     * @param targetGroupId 修改为
     */
    void updateGroupIdByUserId(Long groupId, Long targetGroupId);

    /**
     * 删除该分组下重复的博客ID
     *
     * @param groupId 分组ID
     */
    void deleteDuplicateBlogIdByGroupId(Long groupId);

    /**
     * 添加博客到指定收藏组
     *
     * @param groupId 收藏组ID
     * @param blogId  博客ID
     * @return .
     */
    long insertFavoriteBolg(Long groupId, Long blogId);

    FavoriteBlog selectById(Long id);

    long deleteById(Long id);

    boolean selectAlreadyCollected(Long blogId, Long userId);
    
}
