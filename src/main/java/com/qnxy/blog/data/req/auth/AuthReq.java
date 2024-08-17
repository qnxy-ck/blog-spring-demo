package com.qnxy.blog.data.req.auth;

import lombok.Data;

/**
 * 用户认证请求参数
 *
 * @author Qnxy
 */
@Data
public class AuthReq {

    private String username;
    private String password;

}
