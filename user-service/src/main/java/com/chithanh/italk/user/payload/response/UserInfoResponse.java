package com.chithanh.italk.user.payload.response;

import com.chithanh.italk.user.domain.UserInfo;
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
public class UserInfoResponse {
    private UUID userId;
    private String name;
    private String avatar;
    public static UserInfoResponse toResponse(UserInfo userInfo) {
        return UserInfoResponse.builder()
                .userId(userInfo.getUserId())
                .name( userInfo.getFirstName() + " " + userInfo.getLastName())
                .avatar(userInfo.getAvatar())
                .build();
    }
}