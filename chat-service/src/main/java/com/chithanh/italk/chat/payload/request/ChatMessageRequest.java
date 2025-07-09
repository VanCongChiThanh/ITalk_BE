package com.chithanh.italk.chat.payload.request;

import com.chithanh.italk.chat.domain.enums.MessageType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageRequest {
    private String content;
    private MessageType type;
}