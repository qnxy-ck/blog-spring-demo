package com.qnxy.blog.data.req;

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
