package com.chithanh.italk.chat.payload.response;

import com.chithanh.italk.chat.domain.Community;
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
public class CommunityResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private String avatarUrl;
    private String coverUrl;
    private String slug;
    private boolean isPublic;
    public static CommunityResponse toResponse(Community community) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .ownerId(community.getOwnerId())
                .avatarUrl(community.getAvatarUrl())
                .coverUrl(community.getCoverUrl())
                .slug(community.getSlug())
                .isPublic(community.isPublic())
                .build();
    }
}