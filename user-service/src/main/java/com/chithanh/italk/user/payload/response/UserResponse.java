package com.chithanh.italk.user.payload.response;

import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.enums.ActiveStatus;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private String role;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private boolean active;

    private Timestamp confirmedAt;
    private Timestamp createdAt;
    private Timestamp deletedAt;

  /**
   * Create user response with main information
   *
   * @param user {@link User}
   * @return UserResponse
   */
  public static UserResponse createUserResponseWithMainInfo(User user, AuthProvider provider) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getSubRole())
        .provider(provider)
        .active(user.getIsEnabled())
        .activeStatus(user.getActiveStatus())
        .build();
  }
}