package com.qnxy.blog.controller.auth;

import com.qnxy.blog.core.IgnoreAuth;
import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.service.AuthorizeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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

