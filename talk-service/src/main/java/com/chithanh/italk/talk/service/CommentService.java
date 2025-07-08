package com.chithanh.italk.talk.service;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.enums.TargetType;
import com.chithanh.italk.talk.payload.request.CommentRequest;
import com.chithanh.italk.talk.payload.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService {
    CommentResponse createComment(UUID userId, TargetType targetType, UUID targetId, CommentRequest commentRequest);
    CommentResponse updateComment(UUID userId,UUID commentId, CommentRequest commentRequest);
    Page<CommentResponse> getCommentsByTarget(TargetType targetType, UUID targetId, Pageable pageable);

}