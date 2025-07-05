package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.UserFollow;
import com.chithanh.italk.talk.payload.response.FollowerResponse;
import com.chithanh.italk.talk.payload.response.FollowingResponse;
import com.chithanh.italk.talk.repository.UserFollowRepository;
import com.chithanh.italk.talk.service.UserFollowService;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.service.UserInfoService;
import com.chithanh.italk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {
    private final UserInfoService userInfoService;
    private final UserFollowRepository userFollowRepository;
    @Override
    public UserFollow followUser(UUID followerId, UUID followingId) {
        UserFollow userFollow = createUserFollow(followerId, followingId);
        return userFollowRepository.save(userFollow);
    }

    @Override
    public UserFollow unfollowUser(UUID followerId, UUID followingId) {
        UserFollow userFollow = userFollowRepository.findByFollowingIdAndFollowerId(followerId, followingId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.USER_FOLLOW_NOT_FOUND));
        userFollowRepository.delete(userFollow);
        return userFollow;
    }

    @Override
    public Page<FollowerResponse> getFollowers(UUID userId, Pageable pageable) {
        Page<UserFollow> userFollows = userFollowRepository.findByFollowingId(userId, pageable);
        if (userFollows.isEmpty()) {
            return Page.empty(pageable);
        }
        List<UUID> followerIds = userFollows.stream()
                .map(UserFollow::getFollowerId)
                .toList();
        Map<UUID, UserInfo> userInfoMap = userInfoService.getAllUserInfos(followerIds)
                .stream()
                .collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
        Page<FollowerResponse> responsePage = userFollows.map(uf -> {
            String followerName = userInfoMap.get(uf.getFollowerId()).getFirstName() + " " +
                    userInfoMap.get(uf.getFollowerId()).getLastName();
            String avatar = userInfoMap.get(uf.getFollowerId()).getAvatar();
            return FollowerResponse.toResponse(uf.getFollowerId(), followerName,avatar);
        });
        return responsePage;
    }

    @Override
    public Page<FollowingResponse> getFollowings(UUID userId, Pageable pageable) {
        Page<UserFollow> userFollows = userFollowRepository.findByFollowerId(userId, pageable);
        if (userFollows.isEmpty()) {
            return Page.empty(pageable);
        }
        List<UUID> followingIds = userFollows.stream()
                .map(UserFollow::getFollowingId)
                .toList();
        Map<UUID, UserInfo> userInfoMap = userInfoService.getAllUserInfos(followingIds)
                .stream()
                .collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
        Page<FollowingResponse> responsePage = userFollows.map(uf -> {
            String followingName = userInfoMap.get(uf.getFollowingId()).getFirstName() + " " +
                    userInfoMap.get(uf.getFollowingId()).getLastName();
            String avatar = userInfoMap.get(uf.getFollowingId()).getAvatar();
            return FollowingResponse.toResponse(uf.getFollowingId(), followingName, avatar);
        });
        return responsePage;
    }
    private UserFollow createUserFollow(UUID followerId, UUID followingId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(followerId);
        userFollow.setFollowingId(followingId);
        userFollow.setFollowedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return userFollow;
    }
}