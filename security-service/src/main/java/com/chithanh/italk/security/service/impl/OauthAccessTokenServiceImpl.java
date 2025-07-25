package com.chithanh.italk.security.service.impl;


import com.chithanh.italk.common.common.CommonFunction;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.OauthAccessToken;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.repository.OauthAccessTokenRepository;
import com.chithanh.italk.security.service.OauthAccessTokenService;
import com.chithanh.italk.security.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthAccessTokenServiceImpl implements OauthAccessTokenService {

  private final OauthAccessTokenRepository oauthAccessTokenRepository;

  private final UserAuthService userAuthService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public OauthAccessToken createToken(UUID userId, UUID refreshToken, AuthProvider provider) {
    try {
      OauthAccessToken oauthAccessToken = new OauthAccessToken();
      oauthAccessToken.setUser(userAuthService.findById(userId));
      oauthAccessToken.setRefreshToken(
          Objects.requireNonNullElseGet(refreshToken, UUID::randomUUID));
      oauthAccessToken.setProvider(provider);
      return oauthAccessTokenRepository.save(oauthAccessToken);
    } catch (Exception e) {
      throw new ForbiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public OauthAccessToken getOauthAccessTokenById(UUID id) {
    return oauthAccessTokenRepository
        .findById(id)
        .orElseThrow(() -> new ForbiddenException(MessageConstant.INVALID_TOKEN));
  }

  @Override
  public OauthAccessToken getOauthAccessTokenByRefreshToken(UUID id) {
    OauthAccessToken oauthAccessToken =
        oauthAccessTokenRepository
            .findByRefreshToken(id)
            .orElseThrow(() -> new ForbiddenException(MessageConstant.INVALID_REFRESH_TOKEN));
    if (oauthAccessToken.getRevokedAt() != null) {
      throw new ForbiddenException(MessageConstant.REVOKED_TOKEN);
    }
    oauthAccessToken.setRevokedAt(CommonFunction.getCurrentDateTime());
    oauthAccessToken.setRefreshToken(null);
    return oauthAccessTokenRepository.save(oauthAccessToken);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void revoke(OauthAccessToken oauthAccessToken) {
    oauthAccessToken.setRevokedAt(CommonFunction.getCurrentDateTime());
    oauthAccessTokenRepository.save(oauthAccessToken);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void revokeAll(OauthAccessToken oauthAccessToken, String email) {
    User user =
        userAuthService
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    if (oauthAccessToken == null) {
      oauthAccessTokenRepository.revokeAll(user.getId());
    } else {
      oauthAccessTokenRepository.revokeAll(user.getId(), oauthAccessToken.getId());
    }
  }
}