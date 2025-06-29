package com.chithanh.italk.security.service;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return UserPrincipal.create(user);
  }
}