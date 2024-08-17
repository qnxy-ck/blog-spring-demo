package com.qnxy.blog.controller.user;

import com.qnxy.blog.core.annotations.IgnoreAuth;
import com.qnxy.blog.data.req.user.RegisterInfoReq;
import com.qnxy.blog.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {

    private final UserInfoService userInfoService;


    /**
     * 用户账号注册
     */
    @PostMapping("/register")
    @IgnoreAuth
    public void register(@RequestBody @Validated RegisterInfoReq registerInfoReq) {
        this.userInfoService.registerAccount(registerInfoReq);
    }


}
