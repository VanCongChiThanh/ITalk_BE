package com.chithanh.italk.notification.service.impl;

import com.chithanh.italk.common.constant.CommonConstant;
import com.chithanh.italk.notification.domain.Notification;
import com.chithanh.italk.notification.payload.response.PushNotificationResponse;
import com.chithanh.italk.notification.service.NotificationService;
import com.chithanh.italk.notification.service.PushNotificationService;
import com.chithanh.italk.notification.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {
  private final NotificationService notificationService;
  private final RabbitTemplate rabbitTemplate;

  @Override
  @Async("asyncExecutor")
  public void pushNotification(
          String notificationPosition,
          String notificationType,
          String notificationAction,
          List<UUID> receiverIds,
          Object data) {

    for (UUID receiverId : receiverIds) {
      try {
        // Build payload
        PushNotificationResponse pushNotificationResponse = PushNotificationResponse.builder()
                .receiverId(receiverId)
                .position(notificationPosition)
                .type(notificationType)
                .action(notificationAction)
                .data(data)
                .build();

        // Lưu DB
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .position(notificationPosition)
                .type(notificationType)
                .action(notificationAction)
                .data(JsonUtils.toJson(data))
                .isRead(false)
                .build();
        notificationService.saveNotification(notification);

        rabbitTemplate.convertAndSend(
                CommonConstant.NOTIFICATION_EXCHANGE,
                "",
                pushNotificationResponse
        );

        log.info("Pushed notification to user {} via RabbitMQ", receiverId);

      } catch (Exception e) {
        log.error("Failed to push notification for user {}: {}", receiverId, e.getMessage(), e);
      }
    }
  }
}