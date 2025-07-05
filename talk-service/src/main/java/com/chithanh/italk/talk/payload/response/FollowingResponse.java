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
public class FollowingResponse {
    private UUID followingId;
    private String followingName;
    private String followingAvatar;

    public static FollowingResponse toResponse(UUID followingId, String followingName, String followingAvatar) {
        return FollowingResponse.builder()
                .followingId(followingId)
                .followingName(followingName)
                .followingAvatar(followingAvatar)
                .build();
    }
}