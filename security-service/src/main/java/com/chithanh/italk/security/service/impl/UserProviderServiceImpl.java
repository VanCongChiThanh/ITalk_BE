package com.chithanh.italk.security.service.impl;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserProvider;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.repository.UserProviderRepository;
import com.chithanh.italk.security.service.UserProviderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProviderServiceImpl implements UserProviderService {
  private final UserProviderRepository userProviderRepository;

  @Override
  public void create(AuthProvider provider, String providerId, String email, User user) {
    UserProvider userProvider = new UserProvider();
    userProvider.setProvider(provider);
    userProvider.setProviderId(providerId);
    userProvider.setEmail(email);
    userProvider.setUser(user);
    userProviderRepository.save(userProvider);
  }



  @Override
  public Optional<UserProvider> findByProvider(
      String providerId, AuthProvider authProvider, String email) {
    return userProviderRepository.findByProviderIdAndProviderAndEmail(
        providerId, authProvider, email);
  }

  @Override
  public Optional<UserProvider> findByEmail(String email) {
    return userProviderRepository.findFirstByEmailOrderByCreatedAtDesc(email);
  }

  @Override
  public Optional<UserProvider> findByProviderIdAndProvider(String googleId, AuthProvider authProvider) {
    return userProviderRepository.findByProviderIdAndProvider(googleId, authProvider);
  }
}