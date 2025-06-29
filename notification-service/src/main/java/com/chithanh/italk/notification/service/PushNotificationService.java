package com.chithanh.italk.notification.service;


import java.util.UUID;

public interface PushNotificationService {

  void pushNotification(
          String notificationPosition,
          String notificationType,
          String notificationAction,
          UUID receiverId,
          Object data);
}