package com.chithanh.italk.web.endpoint.newsfeed;

import com.chithanh.italk.common.payload.general.PageInfo;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.utils.PagingUtils;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.talk.domain.enums.TargetType;
import com.chithanh.italk.talk.payload.request.CommentRequest;
import com.chithanh.italk.talk.payload.response.CommentResponse;
import com.chithanh.italk.talk.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comments/create")
    @ApiOperation("Create a new comment on a post")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> createCommentOnPost(
            @PathVariable UUID postId,
            @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You must be logged in to create a comment"));
        }
        CommentResponse commentResponse = commentService.createComment(
                userPrincipal.getId(),
                TargetType.POST,
                postId,
                commentRequest
        );
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(commentResponse));
    }
    @GetMapping("/posts/{postId}/comments")
    @ApiOperation("Get comments for a post")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> getCommentsForPost(
            @PathVariable UUID postId,
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "5") int paging) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<CommentResponse> commentsPage = commentService.getCommentsByTarget(TargetType.POST, postId, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, commentsPage.getTotalPages(), commentsPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(commentsPage.getContent(), pageInfo));
    }

    @PostMapping("/comments/{commentId}/reply")
    @ApiOperation("Reply to a comment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> replyToComment(
            @PathVariable UUID commentId,
            @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You must be logged in to reply to a comment"));
        }
        CommentResponse commentResponse = commentService.createComment(
                userPrincipal.getId(),
                TargetType.COMMENT,
                commentId,
                commentRequest
        );
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(commentResponse));
    }
    //api get replies for a comment
    @GetMapping("/comments/{commentId}/replies")
    @ApiOperation("Get replies for a comment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> getRepliesForComment(
            @PathVariable UUID commentId,
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "5") int paging) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<CommentResponse> repliesPage = commentService.getCommentsByTarget(TargetType.COMMENT, commentId, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, repliesPage.getTotalPages(), repliesPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(repliesPage.getContent(), pageInfo));
    }
    @PostMapping("/reels/{reelId}/comments/create")
    @ApiOperation("Create a new comment on a reel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> createCommentOnReel(
            @PathVariable UUID reelId,
            @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You must be logged in to create a comment"));
        }
        CommentResponse commentResponse = commentService.createComment(
                userPrincipal.getId(),
                TargetType.REEL,
                reelId,
                commentRequest
        );
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(commentResponse));
    }
    @GetMapping("/reels/{reelId}/comments")
    @ApiOperation("Get comments for a reel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> getCommentsForReel(
            @PathVariable UUID reelId,
            @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "paging", defaultValue = "5") int paging) {
        Pageable pageable = PagingUtils.makePageRequest(sortBy, order, page, paging);
        Page<CommentResponse> commentsPage = commentService.getCommentsByTarget(TargetType.REEL, reelId, pageable);
        PageInfo pageInfo =
                new PageInfo(pageable.getPageNumber() + 1, commentsPage.getTotalPages(), commentsPage.getTotalElements());
        return ResponseEntity.ok(ResponseDataAPI.success(commentsPage.getContent(), pageInfo
        ));
    }
    @PatchMapping("/comments/{commentId}")
    @ApiOperation("Update a comment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDataAPI> updateComment(
            @PathVariable UUID commentId,
            @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body(ResponseDataAPI.error("You must be logged in to update a comment"));
        }
        CommentResponse updatedComment = commentService.updateComment(userPrincipal.getId(),commentId, commentRequest);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(updatedComment));
    }
}