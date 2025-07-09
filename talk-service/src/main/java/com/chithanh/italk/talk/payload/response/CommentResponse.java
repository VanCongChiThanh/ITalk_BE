package com.chithanh.italk.talk.payload.response;

import com.chithanh.italk.talk.domain.Comment;
import com.chithanh.italk.talk.domain.enums.TargetType;
import com.chithanh.italk.user.payload.response.UserInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private UUID id;
    private String content;
    private TargetType targetType; // VIDEO, CHALLENGE, COMMENT
    private UUID targetId; // ID of the reel, submission or comment being replied to
    private UserInfoResponse user; // User who made the comment
    public static CommentResponse toResponse(Comment comment, UserInfoResponse user) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .targetType(comment.getTargetType())
                .targetId(comment.getTargetId())
                .user(user)
                .build();
    }
}