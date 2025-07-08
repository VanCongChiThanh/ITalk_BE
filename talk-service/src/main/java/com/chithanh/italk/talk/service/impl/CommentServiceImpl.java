package com.chithanh.italk.talk.service.impl;

import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.talk.domain.Comment;
import com.chithanh.italk.talk.domain.enums.TargetType;
import com.chithanh.italk.talk.payload.request.CommentRequest;
import com.chithanh.italk.talk.payload.response.CommentResponse;
import com.chithanh.italk.talk.payload.response.UserInfoResponse;
import com.chithanh.italk.talk.repository.CommentRepository;
import com.chithanh.italk.talk.service.CommentService;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.service.UserInfoService;
import com.chithanh.italk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserInfoService userInfoService;
    private final UserService userService;

    @Override
    public CommentResponse createComment(UUID userId, TargetType targetType, UUID targetId, CommentRequest commentRequest) {
        User user = userService.findById(userId);
        Comment comment = new Comment();
        comment.setTargetType(targetType);
        comment.setTargetId(targetId);
        comment.setContent(commentRequest.getContent());
        comment.setUser(user); // Assuming user is set in the request
        Comment savedComment = commentRepository.save(comment);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(user.getId());
        return CommentResponse.toResponse(savedComment, UserInfoResponse.toResponse(userInfo));
    }

    @Override
    public CommentResponse updateComment(UUID userId,UUID commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with ID: " + commentId));
        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .userId(updatedComment.getUser().getId())
                .build();
        return CommentResponse.toResponse(updatedComment,userInfoResponse);
    }

    @Override
    public Page<CommentResponse> getCommentsByTarget(TargetType targetType, UUID targetId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByTargetTypeAndTargetId(targetType, targetId, pageable);
        List<UUID> userIds = comments.stream()
                .map(comment -> comment.getUser().getId())
                .distinct()
                .toList();
        List<UserInfo> userInfos = userInfoService.getUserInfosByUserIds(userIds);
        Map<UUID,UserInfo> userInfoMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, Function.identity()));
        return comments.map(comment -> {
            UserInfo userInfo = userInfoMap.get(comment.getUser().getId());
            return CommentResponse.toResponse(comment, UserInfoResponse.toResponse(userInfo));
        });
    }
}