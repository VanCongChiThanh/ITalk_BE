package com.chithanh.italk.notification.consumer;

import com.chithanh.italk.notification.payload.response.PushNotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "notifications.queue")
    public void receiveNotification(PushNotificationResponse notification) {
        try {
            UUID receiverId = notification.getReceiverId();
            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + receiverId,
                    notification
            );
            log.info("Sent notification to user {} via WebSocket", receiverId);
        } catch (Exception e) {
            log.error("Failed to send notification via WebSocket", e);
        }
    }
}