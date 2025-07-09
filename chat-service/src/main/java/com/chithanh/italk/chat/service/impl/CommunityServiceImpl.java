package com.chithanh.italk.chat.service.impl;

import com.chithanh.italk.chat.domain.Community;
import com.chithanh.italk.chat.domain.CommunityMember;
import com.chithanh.italk.chat.domain.enums.CommunityRole;
import com.chithanh.italk.chat.domain.enums.JoinStatus;
import com.chithanh.italk.chat.payload.request.CommunityRequest;
import com.chithanh.italk.chat.payload.response.CommunityResponse;
import com.chithanh.italk.chat.payload.response.JoinCommunityResponse;
import com.chithanh.italk.chat.repository.CommunityMemberRepository;
import com.chithanh.italk.chat.repository.CommunityRepository;
import com.chithanh.italk.chat.service.CommunityService;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;

    @Override
    public CommunityResponse createCommunityByAdmin(CommunityRequest request) {
        Community community = new Community();
        community.setName(request.getName());
        community.setDescription(request.getDescription());
        community.setSlug(request.getSlug());
        community.setOwnerId(request.getOwnerId());
        community.setAvatarUrl(request.getAvatarUrl());
        community.setCoverUrl(request.getCoverUrl());
        community.setPublic(request.isPublic());
        Community savedCommunity = communityRepository.save(community);
        this.createCommunityMember(
                savedCommunity.getId(),
                request.getOwnerId(),
                CommunityRole.OWNER
        );
        return CommunityResponse.toResponse(savedCommunity);
    }
    @Override
    public void addMemberToCommunity(UUID actorId,UUID communityId, UUID userId) {
        if (!checkPermission(communityId, actorId)) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        Optional<CommunityMember> member = communityMemberRepository.findByCommunityIdAndUserId(communityId, userId);
        if (member.isPresent()) {
            throw new RuntimeException("User is already a member of this community");
        }
        CommunityMember newMember= createCommunityMember(communityId, userId, CommunityRole.MEMBER);
    }

    @Override
    public JoinCommunityResponse joinCommunity(UUID actorId, UUID communityId) {
        Community community = findCommunity(communityId);
        JoinCommunityResponse response;
        if (!community.isPublic()) {
            response = JoinCommunityResponse.builder()
                    .joinStatus(JoinStatus.PENDING)
                    .build();
        }
        else {
            CommunityMember member = findCommunityMember(communityId, actorId);
            if (member != null) {
                response = JoinCommunityResponse.builder()
                        .joinStatus(JoinStatus.ALREADY_MEMBER)
                        .build();
            } else {
                member = new CommunityMember();
                member.setCommunityId(communityId);
                member.setUserId(actorId);
                member.setRole(CommunityRole.MEMBER);
                member.setJoinedAt(Timestamp.from(Instant.now()));
                communityMemberRepository.save(member);
                response = JoinCommunityResponse.builder()
                        .joinStatus(JoinStatus.ACCEPTED)
                        .build();
            }
        }
        return response;
    }

    private CommunityMember findCommunityMember(UUID communityId, UUID userId) {
        return communityMemberRepository.findByCommunityIdAndUserId(communityId, userId)
                .orElseThrow(() -> new RuntimeException("Community member not found"));
    }
    private Community findCommunity(UUID communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }
    private boolean checkPermission(UUID communityId, UUID userId) {
        CommunityMember member = communityMemberRepository.findByCommunityIdAndUserId(communityId, userId)
                .orElseThrow(() -> new ForbiddenException(MessageConstant.FORBIDDEN_ERROR));
        CommunityRole role = member.getRole();
        return role == CommunityRole.ADMIN || role == CommunityRole.OWNER;
    }
    private CommunityMember createCommunityMember(UUID communityId, UUID userId,CommunityRole role) {
        CommunityMember member = new CommunityMember();
        member.setCommunityId(communityId);
        member.setUserId(userId);
        member.setRole(role);
        member.setJoinedAt(Timestamp.from(Instant.now()));
        return communityMemberRepository.save(member);
    }
}