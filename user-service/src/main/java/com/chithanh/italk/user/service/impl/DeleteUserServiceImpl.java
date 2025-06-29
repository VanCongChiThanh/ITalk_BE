package com.chithanh.italk.user.service.impl;

import com.chithanh.italk.user.service.DeleteUserService;
import com.chithanh.italk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteUserServiceImpl implements DeleteUserService {
  private final UserService userService;

  @Override
  public void delete(UUID id, String token) {
    userService.removeAccount(id, token);
  }
}