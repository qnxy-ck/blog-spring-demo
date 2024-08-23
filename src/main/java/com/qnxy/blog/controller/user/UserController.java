package com.qnxy.blog.controller.user;

import com.qnxy.blog.core.annotations.IgnoreAuth;
import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.data.req.user.RegisterInfoReq;
import com.qnxy.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
public class UserController {

    private final UserService userService;


    /**
     * 用户登录授权
     * 成功返回 jwt 签名
     */
    @PostMapping("/login")
    @IgnoreAuth
    public String login(@RequestBody AuthReq authReq) {
        return this.userService.login(authReq);
    }


    /**
     * 用户账号注册
     */
    @PostMapping("/register")
    @IgnoreAuth
    public void register(@RequestBody @Validated RegisterInfoReq registerInfoReq) {
        this.userService.registerAccount(registerInfoReq);
    }


}

