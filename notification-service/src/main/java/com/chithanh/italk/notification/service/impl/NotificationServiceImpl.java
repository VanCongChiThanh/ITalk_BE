package com.chithanh.italk.notification.service.impl;

import com.chithanh.italk.notification.domain.Notification;
import com.chithanh.italk.notification.payload.response.NotificationResponse;
import com.chithanh.italk.notification.repository.NotificationRepository;
import com.chithanh.italk.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    @Override
    public NotificationResponse saveNotification(Notification notification) {
        try {
            return NotificationResponse.toResponse(notificationRepository.save(notification));
        } catch (Exception e) {
            log.error("Failed to save notification", e);
            return null;
        }
    }

    @Override
    public void markNotificationAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public void maskAllNotificationsAsRead(UUID userId) {
        notificationRepository.findAllByReceiverIdAndRead(userId, false)
                .forEach(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    @Override
    public List<NotificationResponse> getAllNotificationsByUserId(UUID userId) {
        return notificationRepository.findAllByReceiverIdAndRead(userId, false)
                .stream()
                .map(NotificationResponse::toResponse)
                .toList();
    }

    @Override
    public Integer countUnreadNotificationsByUserId(UUID userId) {
        return notificationRepository.countByReceiverIdAndRead(userId, false);
    }

}