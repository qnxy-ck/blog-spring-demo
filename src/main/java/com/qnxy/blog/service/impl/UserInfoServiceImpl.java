package com.qnxy.blog.service.impl;

import com.qnxy.blog.data.entity.UserInfo;
import com.qnxy.blog.data.req.user.RegisterInfoReq;
import com.qnxy.blog.mapper.UserInfoMapper;
import com.qnxy.blog.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.qnxy.blog.core.ExceptionHandler.expectFalse;
import static com.qnxy.blog.core.ResultUtil.insertSuc;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.ACCOUNT_NAME_ALREADY_EXISTS;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    @Override
    public void registerAccount(RegisterInfoReq registerInfoReq) {
        expectFalse(this.userInfoMapper.selectExistByUsername(registerInfoReq.getUsername()), ACCOUNT_NAME_ALREADY_EXISTS);

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerInfoReq, userInfo);

        insertSuc(this.userInfoMapper.insertUserInfo(userInfo));
    }
}
