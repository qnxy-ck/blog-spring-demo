package com.qnxy.blog.mapper;

import com.qnxy.blog.data.resp.UserFollowerInfoResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Qnxy
 */
@Mapper
public interface FollowInfoMapper {


    List<UserFollowerInfoResp> selectFollowListByUserId(RowBounds rowBounds, Long userId);

    /**
     * 取消关注
     */
    boolean deleteFollowInfo(Long userId, Long followId);

    /**
     * 增加关注
     */
    boolean insertFollowInfo(Long userId, Long followId);
    
    
}
