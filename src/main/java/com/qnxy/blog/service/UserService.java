package com.qnxy.blog.service;

import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.data.req.user.RegisterInfoReq;

/**
 * @author Qnxy
 */
public interface UserService {

    /**
     * 用户登录认证
     */
    String login(AuthReq authReq);

    /**
     * 用户注册
     *
     * @param registerInfoReq 注册信息
     */
    void registerAccount(RegisterInfoReq registerInfoReq);

    /**
     * 校验 JWT Token 信息并返回用户id
     *
     * @param token token 字符串
     * @return 用户ID
     */
    Long checkJwtTokenAndParse(String token);


}
