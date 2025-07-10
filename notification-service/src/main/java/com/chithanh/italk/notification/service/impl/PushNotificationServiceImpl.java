package com.chithanh.italk.notification.service.impl;

import com.chithanh.italk.common.constant.CommonConstant;
import com.chithanh.italk.notification.payload.response.PushNotificationResponse;
import com.chithanh.italk.notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

  private final RabbitTemplate rabbitTemplate;

  @Override
  @Async("asyncExecutor")
  public void pushNotification(
      String notificationPosition,
      String notificationType,
      String notificationAction,
      UUID receiverId,
      Object data) {
    PushNotificationResponse pushNotificationResponse = new PushNotificationResponse();

    toPushNotificationResponse(
        pushNotificationResponse, notificationPosition, notificationType, notificationAction, data);

    // Push notification on rabbitmq
    try {
      String exchangeName = CommonConstant.NOTIFICATION_EXCHANGE_NAME_PREFIX + receiverId;
      if (this.checkExchangeExist(exchangeName)) {
        rabbitTemplate.convertAndSend(
            exchangeName, receiverId.toString(), pushNotificationResponse);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private boolean checkExchangeExist(String exchange) {
    try {
      rabbitTemplate.execute(channel -> channel.exchangeDeclarePassive(exchange));
      return true;
    } catch (Exception e) {
      return false;
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