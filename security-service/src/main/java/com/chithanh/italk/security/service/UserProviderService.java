package com.chithanh.italk.security.service;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserProvider;
import com.chithanh.italk.security.domain.enums.AuthProvider;


import java.util.Optional;

public interface UserProviderService {
  void create(AuthProvider provider, String providerId, String email, User user);

  Optional<UserProvider> findByProvider(String providerId, AuthProvider authProvider, String email);

  Optional<UserProvider> findByEmail(String email);

  Optional<UserProvider> findByProviderIdAndProvider(String googleId, AuthProvider authProvider);
}