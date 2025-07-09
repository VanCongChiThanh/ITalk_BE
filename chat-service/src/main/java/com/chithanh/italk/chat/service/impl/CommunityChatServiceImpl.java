package com.chithanh.italk.chat.service.impl;

import com.chithanh.italk.chat.domain.Message;
import com.chithanh.italk.chat.payload.response.ChatMessageResponse;
import com.chithanh.italk.chat.repository.MessageRepository;
import com.chithanh.italk.chat.service.CommunityChatService;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.payload.response.UserInfoResponse;
import com.chithanh.italk.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityChatServiceImpl implements CommunityChatService {
    private final MessageRepository messageRepository;
    private final UserInfoService userInfoService;
    @Override
    public ChatMessageResponse saveAndReturn(Message message) {
        Message savedMessage = messageRepository.save(message);
        UserInfo senderInfo = userInfoService.getUserInfoByUserId(savedMessage.getSenderId());
        return ChatMessageResponse.builder()
                .id(savedMessage.getId())
                .communityId(savedMessage.getCommunityId())
                .senderId(savedMessage.getSenderId())
                .content(savedMessage.getContent())
                .type(savedMessage.getType())
                .createdAt(savedMessage.getCreatedAt())
                .senderInfo(UserInfoResponse.toResponse(senderInfo))
                .build();
    }

    @Override
    public List<ChatMessageResponse> getMessagesByCommunityId(UUID communityId, Instant before, Pageable pageable) {
       List<Message> messages = messageRepository.findRecentMessages(communityId, before, pageable);
       List<UUID> senderIds = messages.stream()
                .map(Message::getSenderId)
                .distinct()
                .toList();
       List<UserInfo> senderInfos = userInfoService.getUserInfosByUserIds(senderIds);
       Map<UUID, UserInfoResponse> senderInfoMap = senderInfos.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfoResponse::toResponse));
       List<ChatMessageResponse> responses = messages.stream()
                .map(message -> ChatMessageResponse.toResponse(message, senderInfoMap.get(message.getSenderId())))
                .collect(Collectors.toList());
       return responses;
    }
}