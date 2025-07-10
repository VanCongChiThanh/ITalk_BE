package com.chithanh.italk.chat.payload.response;

import com.chithanh.italk.chat.domain.enums.JoinStatus;
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
public class JoinCommunityResponse {
    private UUID communityId;
    private UUID userId;
    private JoinStatus joinStatus;
}