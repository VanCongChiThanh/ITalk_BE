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
import com.chithanh.italk.common.domain.enums.NotificationPosition;
import com.chithanh.italk.common.exception.BadRequestException;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.notification.enums.NotificationAction;
import com.chithanh.italk.notification.enums.NotificationType;
import com.chithanh.italk.notification.payload.NotificationPayload;
import com.chithanh.italk.notification.service.PushNotificationService;
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
    private final PushNotificationService pushNotificationService;

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
    public JoinCommunityResponse addMemberToCommunity(UUID actorId,UUID communityId, UUID userId) {
        if (!checkPermission(communityId, actorId)) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        Community community = findCommunity(communityId);
        Optional<CommunityMember> member = communityMemberRepository.findByCommunityIdAndUserId(communityId, userId);
        if (member.isPresent()) {
            throw new BadRequestException(MessageConstant.COMMUNITY_MEMBER_ALREADY_EXISTS);
        }
        this.createCommunityMember(communityId, userId, CommunityRole.MEMBER);
        // push notification to user
        NotificationPayload notificationPayload = NotificationPayload.builder()
                .title("You have been added to a community")
                .body("You have been added to the community: " + community.getName())
                .imageUrl(community.getAvatarUrl())
                .resourceId(community.getSlug())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        this.pushNotificationService.pushNotification(
                NotificationPosition.NOTIFICATION.toString(),
                NotificationType.COMMUNITY.toString(),
                NotificationAction.JOIN_COMMUNITY.toString(),
                userId,
                notificationPayload
                );
        return JoinCommunityResponse.builder()
                .userId(userId)
                .communityId(communityId)
                .joinStatus(JoinStatus.ACCEPTED)
                .build();
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
                        .userId(actorId)
                        .communityId(communityId)
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
                        .userId(actorId)
                        .communityId(communityId)
                        .joinStatus(JoinStatus.ACCEPTED)
                        .build();
            }
        }
        return response;
    }

    @Override
    public JoinCommunityResponse leaveCommunity(UUID actorId, UUID communityId) {
        Optional<CommunityMember> member = communityMemberRepository.findByCommunityIdAndUserId(communityId, actorId);
        if (member.isEmpty()) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        communityMemberRepository.delete(member.get());
        return JoinCommunityResponse.builder()
                .userId(actorId)
                .communityId(communityId)
                .joinStatus(JoinStatus.LEFT)
                .build();
    }

    private CommunityMember findCommunityMember(UUID communityId, UUID userId) {
        return communityMemberRepository.findByCommunityIdAndUserId(communityId, userId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.COMMUNITY_MEMBER_NOT_FOUND));
    }
    private Community findCommunity(UUID communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.COMMUNITY_NOT_FOUND));
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