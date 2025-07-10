package com.chithanh.italk.notification.payload.response;

import com.chithanh.italk.notification.domain.Notification;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private String position;
    private String type;
    private String action;
    private Object data;
    private boolean isRead;
    private String createdAt;
    public static NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setPosition(notification.getPosition());
        response.setType(notification.getType());
        response.setAction(notification.getAction());
        response.setData(notification.getData());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt().toString());
        return response;
    }
}