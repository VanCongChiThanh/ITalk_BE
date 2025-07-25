package com.chithanh.italk.user.service;

import com.chithanh.italk.user.domain.UserInfo;

import java.util.List;
import java.util.UUID;

public interface UserInfoService {
    UserInfo createUserInfo(UUID userId, String firstname, String lastname,String avatar);
    UserInfo updateUserInfo(UUID userId, String firstname, String lastname, String avatar);
    UserInfo getUserInfoByUserId(UUID userId);
    List<UserInfo> getAllUserInfos(List<UUID> userIds);

    List<UserInfo> getUserInfosByUserIds(List<UUID> userIds);
}