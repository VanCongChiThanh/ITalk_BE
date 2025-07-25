package com.chithanh.italk.security.payload.response;

import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Oauth2Info {
    private AuthProvider authProvider;
    private String providerId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}