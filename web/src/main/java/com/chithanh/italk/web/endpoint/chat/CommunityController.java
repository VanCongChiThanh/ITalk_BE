package com.chithanh.italk.web.endpoint.chat;

import com.chithanh.italk.chat.payload.request.CommunityRequest;
import com.chithanh.italk.chat.payload.response.CommunityResponse;
import com.chithanh.italk.chat.payload.response.JoinCommunityResponse;
import com.chithanh.italk.chat.service.CommunityService;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.domain.enums.Role;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    @PostMapping("/communities")
    @ApiOperation("Create a community by admin")
    public ResponseEntity<ResponseDataAPI> createCommunity(
            @RequestBody CommunityRequest communityRequest,
            @CurrentUser UserPrincipal userPrincipal
            ) {
        if(userPrincipal.getRole().equals(Role.ROLE_ADMIN)) {
            CommunityResponse response = communityService.createCommunityByAdmin(communityRequest);
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
        } else {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error(MessageConstant.FORBIDDEN_ERROR));
        }
    }
    @PostMapping("/communities/{communityId}/join")
    @ApiOperation("Join a community")
    public ResponseEntity<ResponseDataAPI> joinCommunity(
            @PathVariable("communityId") UUID communityId,
            @CurrentUser UserPrincipal userPrincipal
    ) {
        JoinCommunityResponse response = communityService.joinCommunity(userPrincipal.getId(), communityId);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }
    @PostMapping("/communities/{communityId}/members/{userId}")
    @ApiOperation("Add a member to a community")
    public ResponseEntity<ResponseDataAPI> addMemberToCommunity(
            @PathVariable("communityId") UUID communityId,
            @PathVariable("userId") UUID userId,
            @CurrentUser UserPrincipal userPrincipal
    ) {
            communityService.addMemberToCommunity(userPrincipal.getId(), communityId, userId);
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(null));
    }
}