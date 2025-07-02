package com.chithanh.italk.security.service;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.payload.response.Oauth2Info;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Oauth2LoginService {
    private final List<Oauth2LoginStrategy> strategies;

    public Oauth2Info login(AuthProvider provider, String code) {
        return strategies.stream()
                .filter(s -> s.getProvider() == provider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provider not supported"))
                .process(code);
    }
}