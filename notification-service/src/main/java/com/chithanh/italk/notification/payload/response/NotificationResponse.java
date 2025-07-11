package com.chithanh.italk.notification.payload.response;

import com.chithanh.italk.notification.domain.Notification;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private UUID receiverId;
    private String position;
    private String type;
    private String action;
    private Object data;
    private boolean isRead;
    private String createdAt;
    public static NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .receiverId(notification.getReceiverId())
                .position(notification.getPosition())
                .type(notification.getType())
                .action(notification.getAction())
                .data(notification.getData())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}