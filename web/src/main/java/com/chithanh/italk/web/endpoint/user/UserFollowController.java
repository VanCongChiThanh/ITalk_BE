package com.chithanh.italk.web.endpoint.user;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.talk.domain.UserFollow;
import com.chithanh.italk.talk.payload.response.FollowerResponse;
import com.chithanh.italk.talk.payload.response.FollowingResponse;
import com.chithanh.italk.talk.payload.response.UserFollowResponse;
import com.chithanh.italk.talk.service.UserFollowService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserFollowController {
    private final UserFollowService userFollowService;
    @PostMapping("/users/{userId}/follow/{followingId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Follow a user")
    public ResponseEntity<ResponseDataAPI> followUser(
            @PathVariable("userId") UUID userId,
            @PathVariable("followingId") UUID followingId,
            @CurrentUser UserPrincipal userPrincipal
    ) {
        if (userPrincipal == null || !userPrincipal.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You cannot follow on behalf of another user"));
        }
         UserFollow userFollow =userFollowService.followUser(userId, followingId);
         UserFollowResponse response = UserFollowResponse.createUserFollowResponse(userFollow.getFollowerId(), userFollow.getFollowingId(), userFollow.getFollowedAt());
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(response));
    }
    @DeleteMapping("/users/{userId}/unfollow/{followingId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Unfollow a user")
    public ResponseEntity<ResponseDataAPI> unfollowUser(
            @PathVariable("userId") UUID userId,
            @PathVariable("followingId") UUID followingId,
            @CurrentUser UserPrincipal userPrincipal
    ) {
        if (userPrincipal == null || !userPrincipal.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You cannot unfollow on behalf of another user"));
        }
        userFollowService.unfollowUser(userId, followingId);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(null));
    }
    @GetMapping("/users/{userId}/followers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Get all followers of the user")
    public ResponseEntity<ResponseDataAPI> getFollowers(
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "15") int paging,
            @PathVariable("userId") UUID userId
    ) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<FollowerResponse> followersPage = userFollowService.getFollowers(userId, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, followersPage.getTotalPages(), followersPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(followersPage.getContent(), pageInfo));
    }
    @GetMapping("/users/{userId}/following")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Get all users that the user is following")
    public ResponseEntity<ResponseDataAPI> getFollowing(
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "15") int paging,
            @PathVariable("userId") UUID userId
    ) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<FollowingResponse> followingPage = userFollowService.getFollowings(userId, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, followingPage.getTotalPages(), followingPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(followingPage.getContent(), pageInfo));
    }

}