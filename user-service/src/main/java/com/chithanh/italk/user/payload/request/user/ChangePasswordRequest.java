package com.chithanh.italk.user.payload.request.user;

import com.chithanh.italk.common.constant.CommonConstant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChangePasswordRequest {
  @NotBlank private String oldPassword;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String newPassword;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String confirmNewPassword;
}