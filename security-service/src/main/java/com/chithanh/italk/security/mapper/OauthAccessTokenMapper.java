package com.chithanh.italk.security.mapper;

import com.chithanh.italk.common.config.SpringMapStructConfig;
import com.chithanh.italk.security.domain.OauthAccessToken;
import com.chithanh.italk.security.payload.response.OauthAccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface OauthAccessTokenMapper {

    /**
     * Mapper code generator from OauthAccessToken entity to OauthAccessToken response
     *
     * @param oauthAccessToken {@link OauthAccessToken}
     * @param accessToken Access token
     * @param refreshToken Refresh token
     * @param expiresIn Time to expire
     * @return OauthAccessTokenResponse
     */
    @Mapping(source = "accessToken", target = "accessToken")
    @Mapping(source = "refreshToken", target = "refreshToken")
    @Mapping(source = "expiresIn", target = "expiresIn")
    OauthAccessTokenResponse toOauthAccessTokenResponse(
            OauthAccessToken oauthAccessToken, String accessToken, String refreshToken, long expiresIn);
}