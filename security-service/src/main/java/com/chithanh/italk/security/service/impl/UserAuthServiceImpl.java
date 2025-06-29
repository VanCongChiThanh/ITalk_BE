package com.chithanh.italk.security.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.domain.enums.Role;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.repository.UserRepository;
import com.chithanh.italk.security.service.UserAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(UUID id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }

  @Override
  public User registerUserOauth2(String name, String email, String password) {
    User user = new User();

    user.setEmail(email.toLowerCase());
    user.setIsEnabled(true);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole(Role.ROLE_USER);

    return userRepository.save(user);
  }
}