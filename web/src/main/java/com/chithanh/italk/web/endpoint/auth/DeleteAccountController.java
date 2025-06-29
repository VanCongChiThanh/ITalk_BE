package com.chithanh.italk.web.endpoint.auth;


import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.user.payload.request.user.RemoveAccountRequest;
import com.chithanh.italk.user.service.DeleteUserService;
import com.chithanh.italk.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Api(tags = "Delete Account APIs")
public class DeleteAccountController {

  private final UserService userService;

  private final DeleteUserService deleteUserService;

  @PostMapping("/user/request-remove")
  public ResponseEntity<ResponseDataAPI> requestRemoveAccount(
      @CurrentUser UserPrincipal userPrincipal) {
    if (userPrincipal == null) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    userService.requestRemoveAccount(userPrincipal.getId());
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  @PostMapping("/user/remove")
  public ResponseEntity<ResponseDataAPI> removeAccount(
      @CurrentUser UserPrincipal userPrincipal,
      @Valid @RequestBody RemoveAccountRequest removeAccountRequest) {
    if (userPrincipal == null) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    deleteUserService.delete(userPrincipal.getId(), removeAccountRequest.getToken());

    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}