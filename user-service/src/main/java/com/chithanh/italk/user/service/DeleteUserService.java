package com.chithanh.italk.user.service;

import java.util.UUID;

public interface DeleteUserService {
  void delete(UUID id, String token);
}