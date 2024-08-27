package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.FavoriteBlogGroup;
import com.qnxy.blog.data.req.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Qnxy
 */
@Mapper
public interface FavoriteBlogGroupMapper {

    /**
     * 增加分组
     */
    long insertFavoriteBlogGroup(Long userId, FavoriteBolgGroupReq groupParam);


    /**
     * 查询是否存在
     * 存在返回 true
     */
    boolean selectUserIdAndNameExists(Long userId, String name);


    List<FavoriteBlogGroupResp> selectGroupListByUserId(RowBounds rowBounds, Long userId);

    boolean insertDefaultFavoriteBlogGroup(Long userId, String defaultName);

    long updateGroupInfo(Long userId, @Param("info") FavoriteBolgGroupReq favoriteBolgGroupReq);

    FavoriteBlogGroup selectGroupById(Long groupId);

    /**
     * 查询某个用户的默认分组
     */
    FavoriteBlogGroup selectDefaultGroupByUserId(Long userId);

    FavoriteBlogGroup selectGroupByIdAndUserId(Long groupId, Long userId);

}
