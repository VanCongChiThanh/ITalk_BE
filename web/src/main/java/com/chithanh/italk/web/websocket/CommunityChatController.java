package com.chithanh.italk.web.websocket;

import com.chithanh.italk.chat.domain.Message;
import com.chithanh.italk.chat.payload.request.ChatMessageRequest;
import com.chithanh.italk.chat.payload.response.ChatMessageResponse;
import com.chithanh.italk.chat.service.CommunityChatService;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommunityChatController {

    private final CommunityChatService communityChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/community/{communityId}/send")
    public void sendMessage(@DestinationVariable UUID communityId,
                            ChatMessageRequest request,
                            @CurrentUser UserPrincipal userPrincipal
                            ) {
        Message message = new Message();
        message.setCommunityId(communityId);
        message.setSenderId(userPrincipal.getId());
        message.setContent(request.getContent());
        message.setType(request.getType());

        ChatMessageResponse response = communityChatService.saveAndReturn(message);

        messagingTemplate.convertAndSend("/topic/community/" + communityId, response);
    }
}