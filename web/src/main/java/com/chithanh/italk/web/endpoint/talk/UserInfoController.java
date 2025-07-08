package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.payload.request.profile.UserProfileRequest;
import com.chithanh.italk.user.payload.response.UserProfileResponse;
import com.chithanh.italk.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/v1")
@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;
    @PatchMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Update user profile")
    public ResponseEntity<ResponseDataAPI> updateProfile(
            @RequestBody @Valid UserProfileRequest userProfileRequest,
            @CurrentUser UserPrincipal userPrincipal
            ) {
        UserInfo userInfo=userInfoService.updateUserInfo(
                userPrincipal.getId(),
                userProfileRequest.getFirstName(),
                userProfileRequest.getLastName(),
                null
        );
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(UserProfileResponse.toResponse(userInfo)));
    }
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Get current user profile")
    public ResponseEntity<ResponseDataAPI> getCurrentUserProfile(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userPrincipal.getId());
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(UserProfileResponse.toResponse(userInfo)));
    }
    @GetMapping("/users/{userId}/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Get user profile")
    public ResponseEntity<ResponseDataAPI> getUserProfile(
            @PathVariable("userId") UUID userId
    ) {
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(UserProfileResponse.toResponse(userInfo)));
    }
}