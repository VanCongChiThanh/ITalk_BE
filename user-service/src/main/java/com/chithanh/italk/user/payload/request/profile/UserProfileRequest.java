package com.chithanh.italk.user.payload.request.profile;

import com.chithanh.italk.user.domain.enums.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileRequest {
    private String firstName;
    private String lastName;
}