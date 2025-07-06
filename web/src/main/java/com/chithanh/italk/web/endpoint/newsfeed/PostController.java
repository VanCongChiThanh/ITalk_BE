package com.chithanh.italk.web.endpoint.newsfeed;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.talk.domain.enums.PostType;
import com.chithanh.italk.talk.payload.request.PostRequest;
import com.chithanh.italk.talk.payload.response.PostResponse;
import com.chithanh.italk.talk.service.PostService;
import com.chithanh.italk.user.service.UserService;
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
public class PostController {
    private final PostService postService;
    private final UserService userService;
    @PostMapping("users/{userId}/posts")
    @ApiOperation("Create a new post")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> createPost(
            @RequestBody PostRequest postRequest,
            @PathVariable UUID userId,
            @CurrentUser UserPrincipal userPrincipal) {
        if (userPrincipal == null || !userPrincipal.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You cannot create a post on behalf of another user"));
        }
        User user = userService.findById(userId);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(postService.createPost(user,postRequest)));
    }
    @GetMapping("posts/{postType}")
    @ApiOperation("Get all posts by type")
    public ResponseEntity<ResponseDataAPI> getPostsByType(
            @PathVariable String postType,
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "15") int paging
    ) {
        PostType type=PostType.valueOf(postType.toUpperCase());
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<PostResponse> postResponsesPage = postService.getPostsByType(type, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, postResponsesPage.getTotalPages(), postResponsesPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(postResponsesPage.getContent(), pageInfo));
    }
    @PatchMapping("users/{userId}/posts/{postId}")
    @ApiOperation("Update a post")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> updatePost(
            @PathVariable UUID postId,
            @PathVariable UUID userId,
            @RequestBody PostRequest postRequest,
            @CurrentUser UserPrincipal userPrincipal) {
        if (userPrincipal == null || !userPrincipal.getId().equals(userId)) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You cannot create a post on behalf of another user"));
        }
        PostResponse updatedPost = postService.updatePost(postId, postRequest);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(updatedPost));
    }

}