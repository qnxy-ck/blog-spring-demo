package com.qnxy.blog.mapper;

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

}
