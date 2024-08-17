package com.qnxy.blog.controller.auth;

import com.qnxy.blog.core.IgnoreAuth;
import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户授权
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizeController {

    private final AuthorizeService authorizeService;


    /**
     * 用户登录授权
     * 成功返回 jwt 签名
     */
    @PostMapping("/login")
    @IgnoreAuth
    public String login(@RequestBody AuthReq authReq) {
        return this.authorizeService.login(authReq);
    }


}

