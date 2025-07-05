package com.chithanh.italk.talk.payload.response;

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
public class UserFollowResponse {
    private UUID id;
    private UUID followerId;
    private UUID followingId;
    private Timestamp followedAt;
    public static UserFollowResponse createUserFollowResponse(UUID followerId, UUID followingId, Timestamp followedAt) {
        return UserFollowResponse.builder()
                .followerId(followerId)
                .followingId(followingId)
                .followedAt(Timestamp.valueOf(followedAt.toLocalDateTime()))
                .build();
    }
}