package com.chithanh.italk.web.endpoint.community;

import com.chithanh.italk.chat.payload.response.ChatMessageResponse;
import com.chithanh.italk.chat.service.CommunityChatService;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CommunityChatRestController {

    private final CommunityChatService communityChatService;

    @GetMapping("/communities/{communityId}/messages")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @ApiOperation("Get recent messages of a community")
    public ResponseEntity<ResponseDataAPI> getRecentMessages(
            @PathVariable("communityId") UUID communityId,
            @RequestParam(value = "before", required = false) Long beforeTimestamp,
            @RequestParam(value = "limit", defaultValue = "50") int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        Instant before = (beforeTimestamp != null) ?
                Instant.ofEpochMilli(beforeTimestamp) :
                Instant.now();

        List<ChatMessageResponse> messages = communityChatService.getMessagesByCommunityId(communityId, before, pageable);

        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(messages));
    }
}