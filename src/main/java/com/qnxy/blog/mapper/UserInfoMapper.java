package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Qnxy
 */
@Mapper
public interface UserInfoMapper {

    /**
     * 根据用户名密码查询数据, 用于用户登录
     */
    UserInfo selectByUsernameAndPassword(String username, String password);

    UserInfo selectByPrimaryKey(Long id);

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     */
    boolean selectExistByUsername(String username);

    /**
     * 添加用户信息
     *
     * @param userInfo 用户信息
     * @return 添加成功返回true
     */
    boolean insertUserInfo(@Param("userInfo") UserInfo userInfo);

}
