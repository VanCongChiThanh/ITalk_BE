package com.chithanh.italk.security.service;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.payload.response.Oauth2Info;

public interface Oauth2LoginStrategy {
    Oauth2Info process(String accessToken);
    AuthProvider getProvider();
}