package com.chithanh.italk.notification.service.impl;

import com.chithanh.italk.notification.domain.Notification;
import com.chithanh.italk.notification.payload.response.PushNotificationResponse;
import com.chithanh.italk.notification.service.NotificationService;
import com.chithanh.italk.notification.service.PushNotificationService;
import com.chithanh.italk.notification.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

  private final SimpMessagingTemplate messagingTemplate;
//  private final FcmService fcmService;
  private final NotificationService notificationService;

  @Override
  @Async("asyncExecutor")
  public void pushNotification(
          String notificationPosition,
          String notificationType,
          String notificationAction,
          List<UUID> receiverIds,
          Object data
  ) {
    PushNotificationResponse pushNotificationResponse = new PushNotificationResponse();
    toPushNotificationResponse(pushNotificationResponse, notificationPosition, notificationType, notificationAction, data);

    for (UUID receiverId : receiverIds) {
      try {
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .position(notificationPosition)
                .type(notificationType)
                .action(notificationAction)
                .data(JsonUtils.toJson(data))
                .isRead(false)
                .build();
        this.notificationService.saveNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + receiverId, pushNotificationResponse);
      } catch (Exception e) {
        log.error("Failed to push notification to user: {}", receiverId, e);
      }
    }
  }



  private void toPushNotificationResponse(
          PushNotificationResponse pushNotificationResponse,
          String notificationPosition,
          String notificationType,
          String notificationAction,
          Object data) {
    pushNotificationResponse.setPosition(notificationPosition);
    pushNotificationResponse.setType(notificationType);
    pushNotificationResponse.setAction(notificationAction);
    pushNotificationResponse.setData(data);
  }
}