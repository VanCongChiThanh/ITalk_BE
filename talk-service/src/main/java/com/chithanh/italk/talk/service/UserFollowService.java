package com.chithanh.italk.talk.service;

import com.chithanh.italk.talk.domain.UserFollow;
import com.chithanh.italk.talk.payload.response.FollowerResponse;
import com.chithanh.italk.talk.payload.response.FollowingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserFollowService {
    UserFollow followUser(UUID followerId, UUID followingId);
    UserFollow unfollowUser(UUID followerId, UUID followingId);
    Page<FollowerResponse> getFollowers(UUID userId, Pageable pageable);
    Page<FollowingResponse> getFollowings(UUID userId, Pageable pageable);
}