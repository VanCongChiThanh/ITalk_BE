package com.chithanh.italk.chat.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityRequest {
    private String name;
    private UUID ownerId;
    private String description;
    private String avatarUrl;
    private String coverUrl;
    private String slug;
    private boolean isPublic;
}