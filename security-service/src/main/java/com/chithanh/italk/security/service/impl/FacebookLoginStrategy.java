package com.chithanh.italk.security.service.impl;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.payload.response.Oauth2Info;
import com.chithanh.italk.security.service.Oauth2LoginStrategy;
import org.springframework.stereotype.Service;

@Service
public class FacebookLoginStrategy implements Oauth2LoginStrategy {

    @Override
    public Oauth2Info process(String accessToken) {
        // 1. Call Facebook API to get user info (email, id, name)
        // 2. Check UserProvider, if not exists, create new User and UserProvider
        // 3. Return User
        return null; // Replace with actual implementation
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.FACEBOOK; // Return the provider type
    }
}