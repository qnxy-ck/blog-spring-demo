package com.qnxy.blog.service;

import com.qnxy.blog.data.req.auth.AuthReq;

/**
 * @author Qnxy
 */
public interface AuthorizeService {

    /**
     * 用户登录认证
     * 认证通过返回 jwt token
     */
    String login(AuthReq authReq);

    boolean checkJwtToken(String token);
    
    Long userIdFromJwtToken(String token);
    
}
