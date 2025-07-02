package com.chithanh.italk.user.service;

import com.chithanh.italk.user.domain.UserInfo;

import java.util.UUID;

public interface UserInfoService {
    UserInfo createUserInfo(UUID userId, String firstname, String lastname,String avatar);
    UserInfo getUserInfoByUserId(UUID userId);
}