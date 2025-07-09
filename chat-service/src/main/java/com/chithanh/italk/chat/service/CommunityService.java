package com.chithanh.italk.chat.service;

import com.chithanh.italk.chat.payload.request.CommunityRequest;
import com.chithanh.italk.chat.payload.response.CommunityResponse;
import com.chithanh.italk.chat.payload.response.JoinCommunityResponse;

import java.util.UUID;

public interface CommunityService {
    CommunityResponse createCommunityByAdmin(CommunityRequest request);
    void addMemberToCommunity(UUID actorId,UUID communityId, UUID userId);
    JoinCommunityResponse joinCommunity(UUID actorId, UUID communityId);
}