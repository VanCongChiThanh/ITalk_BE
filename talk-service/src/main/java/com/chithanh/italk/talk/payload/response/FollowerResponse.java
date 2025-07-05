package com.chithanh.italk.talk.payload.response;

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
public class FollowerResponse {
    private UUID followerId;
    private String followerName;
    private String followerAvatar;
    public static FollowerResponse toResponse(UUID followerId, String followerName, String followerAvatar) {
        return FollowerResponse.builder()
                .followerId(followerId)
                .followerName(followerName)
                .followerAvatar(followerAvatar)
                .build();
    }
}