package com.qnxy.blog.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qnxy.blog.configuration.auth.AuthenticationConfigurationProperties;
import com.qnxy.blog.core.BeanCopy;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.CommonResultStatusCodeE;
import com.qnxy.blog.data.entity.UserInfo;
import com.qnxy.blog.data.event.UserRegisterEvent;
import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.data.req.user.RegisterInfoReq;
import com.qnxy.blog.data.req.user.UpdateInfoReq;
import com.qnxy.blog.mapper.UserInfoMapper;
import com.qnxy.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.qnxy.blog.core.CommonResultStatusCodeE.INCORRECT_ACCOUNT_OR_PASSWORD;
import static com.qnxy.blog.core.VerificationExpectations.*;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.ACCOUNT_NAME_ALREADY_EXISTS;

/**
 * @author Qnxy
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder DEFAULT_PASSWORD_ENCODER = new BCryptPasswordEncoder(11);

    private final UserInfoMapper userInfoMapper;
    private final AuthenticationConfigurationProperties authenticationConfigurationProperties;
    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public String login(AuthReq authReq) {
        final Long userInfoId = Optional.ofNullable(this.userInfoMapper.selectByUsername(authReq.getUsername()))
                .filter(it -> DEFAULT_PASSWORD_ENCODER.matches(authReq.getPassword(), it.getPassword()))
                .map(UserInfo::getId)
                .orElseThrow(INCORRECT_ACCOUNT_OR_PASSWORD.createSupplierEx());

        final Instant expires = LocalDateTime.now()
                .plusSeconds(this.authenticationConfigurationProperties.getJwtExpirationTime().getSeconds())
                .toInstant(ZoneOffset.ofHours(8));

        return JWT.create()
                .withClaim(this.authenticationConfigurationProperties.getJwtPayloadKey(), userInfoId)
                .withExpiresAt(expires)
                .withIssuer(this.authenticationConfigurationProperties.getJwtIssuer())
                .sign(jwtAlgorithm);
    }


    @Override
    @Transactional
    public void registerAccount(RegisterInfoReq registerInfoReq) {
        expectFalse(this.userInfoMapper.selectExistByUsername(registerInfoReq.getUsername()), ACCOUNT_NAME_ALREADY_EXISTS);

        final UserInfo userInfo = BeanCopy.copyValue(registerInfoReq, new UserInfo());
        final String encodePassword = DEFAULT_PASSWORD_ENCODER.encode(userInfo.getPassword());
        userInfo.setPassword(encodePassword);

        expectInsertOk(this.userInfoMapper.insertUserInfo(userInfo));

        this.applicationEventPublisher.publishEvent(UserRegisterEvent.of(userInfo.getId()));
    }

    @Override
    public Long checkJwtTokenAndParse(String token) {
        try {
            final DecodedJWT verify = this.jwtVerifier.verify(token);
            return verify.getClaim(this.authenticationConfigurationProperties.getJwtPayloadKey()).asLong();
        } catch (JWTVerificationException e) {
            throw new BizException(e, CommonResultStatusCodeE.SIGNATURE_VERIFICATION_EXCEPTION);
        }
    }

    @Override
    public void updateUserInfo(Long userId, UpdateInfoReq updateInfoReq) {
        boolean b = Stream.of(
                        updateInfoReq.getUserAvatar(),
                        updateInfoReq.getProfession(),
                        updateInfoReq.getGender(),
                        updateInfoReq.getPersonalDescription()
                )
                .allMatch(Objects::isNull);

        if (b) {
            // 全部为空, 无需修改
            return;
        }

        expectUpdateOk(this.userInfoMapper.updateUserInfoById(userId, updateInfoReq));
    }

}
