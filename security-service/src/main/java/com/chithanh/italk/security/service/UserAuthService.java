package com.chithanh.italk.security.service;


import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.AuthProvider;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthService {

  /**
   * Get user by email
   *
   * @param email User's email
   * @return Optional {@link User}
   */
  Optional<User> findByEmail(String email);

  /**
   * Get User by id
   *
   * @param id User's id
   * @return User
   */
  User findById(UUID id);


}