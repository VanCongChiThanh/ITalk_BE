package com.chithanh.italk.talk.payload.response;

import com.chithanh.italk.talk.domain.Post;
import com.chithanh.italk.talk.domain.Submission;
import com.chithanh.italk.talk.domain.enums.PostType;
import com.chithanh.italk.user.domain.UserInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    private UUID id;
    private String content;
    private PostType type;
    private SubmissionResponse submission;
    private Timestamp createdAt;
    private UserInfoResponse user;
    public static PostResponse toResponse(Post post, SubmissionResponse submission, UserInfoResponse user) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .type(post.getType())
                .submission(submission)
                .user(user)
                .createdAt(post.getCreatedAt())
                .build();
    }
}