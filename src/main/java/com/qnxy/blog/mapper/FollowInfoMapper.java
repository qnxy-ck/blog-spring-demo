package com.qnxy.blog.mapper;

import com.qnxy.blog.data.resp.UserFollowerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Qnxy
 */
@Mapper
public interface FollowInfoMapper {


    List<UserFollowerInfo> selectFollowListByUserId(RowBounds rowBounds, Long userId);


}
