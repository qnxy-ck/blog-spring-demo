package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Qnxy
 */
@Mapper
public interface UserInfoMapper {

    /**
     * 根据用户名密码查询数据, 用于用户登录
     */
    UserInfo selectByUsernameAndPassword(String username, String password);

    UserInfo selectByPrimaryKey(Integer id);

    boolean selectExistsByUsername(String username);

    boolean insertUserInfo(UserInfo userInfo);
    
}
