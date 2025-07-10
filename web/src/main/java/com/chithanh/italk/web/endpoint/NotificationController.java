package com.chithanh.italk.web.endpoint;

import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.notification.service.NotificationService;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/v1/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/get_list")
    @ApiOperation("Get list of notifications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> getNotifications(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(notificationService.getAllNotificationsByUserId(userPrincipal.getId())));
    }
    @GetMapping("/count_unread")
    @ApiOperation("Count unread notifications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> countUnread(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(notificationService.countUnreadNotificationsByUserId(userPrincipal.getId())));
    }
    @PatchMapping("/mark_as_read")
    @ApiOperation("Mark notifications as read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> markNotificationAsRead(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(required = false)UUID notificationId
            ) {
        if(notificationId != null) {
            notificationService.markNotificationAsRead(notificationId);
        } else {
            notificationService.maskAllNotificationsAsRead(userPrincipal.getId());
        }
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta("All notifications marked as read"));
    }
}