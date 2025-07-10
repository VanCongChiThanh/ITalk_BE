package com.chithanh.italk.notification.service;

import com.chithanh.italk.notification.domain.Notification;
import com.chithanh.italk.notification.payload.response.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse saveNotification(Notification notification);
    void markNotificationAsRead(UUID notificationId);
    void maskAllNotificationsAsRead(UUID userId);
    List<NotificationResponse> getAllNotificationsByUserId(UUID userId);
    Integer countUnreadNotificationsByUserId(UUID userId);
}