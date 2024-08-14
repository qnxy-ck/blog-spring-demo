package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Qnxy
 */
@Mapper
public interface UserInfoMapper {

    List<UserInfo> selectAll();


}
