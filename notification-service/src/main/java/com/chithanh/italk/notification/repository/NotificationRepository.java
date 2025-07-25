package com.chithanh.italk.notification.repository;

import com.chithanh.italk.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByReceiverIdAndIsRead(UUID userId, boolean b);
    Integer countByReceiverIdAndIsRead(UUID userId, boolean b);
}