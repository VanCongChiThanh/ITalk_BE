package com.chithanh.italk.web.endpoint.talk;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.talk.payload.response.FollowerResponse;
import com.chithanh.italk.talk.payload.response.FollowingResponse;
import com.chithanh.italk.talk.service.UserFollowService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @PostMapping("/users/{userId}/follow")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Follow a user")
    public ResponseEntity<ResponseDataAPI> followUser(
            @PathVariable("userId") UUID userId,
            @RequestParam("followingId") UUID followingId
    ) {
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userFollowService.followUser(userId, followingId)));
    }
    @DeleteMapping("/users/{userId}/unfollow")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation("Unfollow a user")
    public ResponseEntity<ResponseDataAPI> unfollowUser(
            @PathVariable("userId") UUID userId,
            @RequestParam("followingId") UUID followingId
    ) {
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(userFollowService.unfollowUser(userId, followingId)));
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