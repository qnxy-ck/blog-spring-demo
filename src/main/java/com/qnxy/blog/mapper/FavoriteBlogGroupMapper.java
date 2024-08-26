package com.qnxy.blog.mapper;

import com.qnxy.blog.data.req.user.FavoriteBolgGroupReq;
import com.qnxy.blog.data.resp.FavoriteBlogGroupResp;
import org.apache.ibatis.annotations.Mapper;
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
}
