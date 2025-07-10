package com.chithanh.italk.notification.service;


import java.util.List;
import java.util.UUID;

public interface PushNotificationService {

  void pushNotification(
          String notificationPosition,
          String notificationType,
          String notificationAction,
          List<UUID> receiverIds,
          Object data
  );
}