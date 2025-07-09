package com.chithanh.italk.chat.service;

import com.chithanh.italk.chat.domain.Message;
import com.chithanh.italk.chat.payload.response.ChatMessageResponse;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CommunityChatService {
    ChatMessageResponse saveAndReturn(Message message);
    List<ChatMessageResponse> getMessagesByCommunityId(UUID communityId, Instant before, Pageable pageable);
}