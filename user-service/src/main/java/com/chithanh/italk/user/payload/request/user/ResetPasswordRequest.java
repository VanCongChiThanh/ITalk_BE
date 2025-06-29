package com.chithanh.italk.user.payload.request.user;

import com.chithanh.italk.common.constant.CommonConstant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResetPasswordRequest {
  @NotBlank @Email private String email;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String password;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String passwordConfirmation;

  @NotNull private UUID resetPasswordToken;
}