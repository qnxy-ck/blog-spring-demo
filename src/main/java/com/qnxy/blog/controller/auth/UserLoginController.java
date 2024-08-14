package com.qnxy.blog.controller.auth;

import com.qnxy.blog.core.ex.BizException;
import com.qnxy.blog.data.R;
import com.qnxy.blog.data.entity.UserInfo;
import com.qnxy.blog.data.enums.CommonResultStatusCodeE;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.qnxy.blog.data.enums.CommonResultStatusCodeE.*;

/**
 * @author Qnxy
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserLoginController {


    @GetMapping("/all")
    public R<List<UserInfo>> getAll() {

        Test.throwException("cyh");
//        throw new BizException(CommonResultStatusCodeE.Test, "cyh");

        
        return null;
    }
}
