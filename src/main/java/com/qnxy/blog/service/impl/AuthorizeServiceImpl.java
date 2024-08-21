package com.qnxy.blog.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qnxy.blog.configuration.auth.AuthenticationConfigurationProperties;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.CommonResultStatusCodeE;
import com.qnxy.blog.data.entity.UserInfo;
import com.qnxy.blog.data.req.auth.AuthReq;
import com.qnxy.blog.mapper.UserInfoMapper;
import com.qnxy.blog.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.qnxy.blog.core.CommonResultStatusCodeE.INCORRECT_ACCOUNT_OR_PASSWORD;

/**
 * @author Qnxy
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizeServiceImpl implements AuthorizeService, InitializingBean {

    private final UserInfoMapper userInfoMapper;
    private final AuthenticationConfigurationProperties authenticationConfigurationProperties;
    private JWTVerifier jwtVerifier;


    @Override
    public String login(AuthReq authReq) {
        final Long userInfoId = Optional.ofNullable(this.userInfoMapper.selectByUsernameAndPassword(authReq.getUsername(), authReq.getPassword()))
                .map(UserInfo::getId)
                .orElseThrow(INCORRECT_ACCOUNT_OR_PASSWORD.createSupplierEx());

        final Instant expires = LocalDateTime.now()
                .plusSeconds(this.authenticationConfigurationProperties.getJwtExpirationTime().getSeconds())
                .toInstant(ZoneOffset.ofHours(8));

        return JWT.create()
                .withClaim(this.authenticationConfigurationProperties.getJwtPayloadKey(), userInfoId)
                .withExpiresAt(expires)
                .withIssuer(this.authenticationConfigurationProperties.getJwtIssuer())
                .sign(Algorithm.HMAC256(this.authenticationConfigurationProperties.getJwtSecret()));
    }

    @Override
    public boolean checkJwtToken(String token) {
        try {
            this.jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new BizException(e, CommonResultStatusCodeE.SIGNATURE_VERIFICATION_EXCEPTION);
        }
        return true;
    }

    @Override
    public Long userIdFromJwtToken(String token) {
        try {
            final DecodedJWT verify = this.jwtVerifier.verify(token);
            return verify.getClaim(this.authenticationConfigurationProperties.getJwtPayloadKey()).asLong();
        } catch (JWTVerificationException e) {
            throw new BizException(e, CommonResultStatusCodeE.SIGNATURE_VERIFICATION_EXCEPTION);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(this.authenticationConfigurationProperties.getJwtSecret());
        this.jwtVerifier = JWT.require(algorithm).build();
    }
}
