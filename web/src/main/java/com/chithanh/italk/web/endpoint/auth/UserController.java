package com.chithanh.italk.web.endpoint.auth;

import com.chithanh.italk.common.common.CommonFunction;
import com.chithanh.italk.common.constant.CommonConstant;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.domain.enums.Role;
import com.chithanh.italk.common.exception.BadRequestException;
import com.chithanh.italk.common.exception.NotFoundException;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.security.annotation.CurrentUser;
import com.chithanh.italk.security.domain.OauthAccessToken;
import com.chithanh.italk.security.domain.User;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.security.domain.enums.AuthProvider;
import com.chithanh.italk.security.token.TokenProvider;
import com.chithanh.italk.user.domain.UserInfo;
import com.chithanh.italk.user.payload.request.user.*;
import com.chithanh.italk.user.payload.response.UserResponse;
import com.chithanh.italk.user.service.UserInfoService;
import com.chithanh.italk.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Api(tags = "User APIs")
public class UserController {

  private final UserService userService;
  private final UserInfoService userInfoService;

  private final TokenProvider tokenProvider;

  private final ConnectionFactory connectionFactory;

  @PostMapping("/user/sign-up")
  public ResponseEntity<ResponseDataAPI> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordConfirmation())) {
      throw new BadRequestException(MessageConstant.PASSWORD_FIELDS_MUST_MATCH);
    }
        userService.registerUser(
            signUpRequest.getFirstname(),
            signUpRequest.getLastname(),
            signUpRequest.getEmail(),
            signUpRequest.getPassword(),
            Role.valueOf(CommonConstant.ROLE_PREFIX.concat(signUpRequest.getRole())));
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  /**
   * Get user
   *
   * @param userPrincipal Current User
   * @return ResponseDataAPI
   */
  @GetMapping("/user")
  public ResponseEntity<ResponseDataAPI> getUser(
          @CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
    User user = userService.findById(userPrincipal.getId());

    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            UserResponse.createUserResponseWithMainInfo(user, this.getProvider(request))));
  }

  /**
   * Change password
   *
   * @param userPrincipal Current user
   * @param changePasswordRequest {@link com.chithanh.italk.user.payload.request.user.ChangePasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  @PostMapping("/user/change_password")
  public ResponseEntity<ResponseDataAPI> changePassword(
      @CurrentUser UserPrincipal userPrincipal,
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
    return ResponseEntity.ok(
        userService.changePassword(
            userPrincipal.getId(), userPrincipal.getPassword(), changePasswordRequest));
  }

  /**
   * Confirm email register
   *
   * @param confirmationToken {@link String} confirmation
   * @return ResponseDataAPI
   */
  @GetMapping("/user/confirmation")
  public ResponseEntity<ResponseDataAPI> confirmEmailRegister(
      @RequestParam(name = "confirmation_token") String confirmationToken) {
    return userService.confirmEmailRegister(confirmationToken);
  }

  /**
   * Forgot password
   *
   * @param forgotPasswordRequest {@link com.chithanh.italk.user.payload.request.user.ForgotPasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  @PostMapping("/user/passwords/forgot")
  public ResponseEntity<ResponseDataAPI> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    return ResponseEntity.ok(userService.forgotPassword(forgotPasswordRequest));
  }

  /**
   * Reset password
   *
   * @param resetPasswordRequest {@link com.chithanh.italk.user.payload.request.user.ResetPasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  @PostMapping("/user/passwords/reset")
  public ResponseEntity<ResponseDataAPI> resetPassword(
      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    return ResponseEntity.ok(userService.resetPassword(resetPasswordRequest));
  }

  /**
   * Resend mail active account
   *
   * @param resendMailActiveRequest {@link ResendMailActiveRequest}
   * @return ResponseDataAPI
   */
  @PostMapping("/user/resend-mail")
  public ResponseEntity<ResponseDataAPI> resendMailActiveAccount(
      @Valid @RequestBody ResendMailActiveRequest resendMailActiveRequest) {
    User user =
        userService
            .findByEmail(resendMailActiveRequest.getEmail())
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    UserInfo userInfo=userInfoService.getUserInfoByUserId(user.getId());
    userService.resendMailActiveRequest(user, userInfo.getFirstName(), userInfo.getLastName());
    return ResponseEntity.ok(ResponseDataAPI.success(null, null));
  }

  /**
   * User create queue on rabbitmq
   *
   * @param userPrincipal Current User
   * @return ResponseDataAPI
   */
  @GetMapping("/notifications/endpoint")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getEndpoint(@CurrentUser UserPrincipal userPrincipal) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

    String queueName = CommonFunction.generateQueueName(userPrincipal.getId());
    rabbitAdmin.declareQueue(new Queue(queueName, false, false, false));

    String exchangeName = CommonConstant.NOTIFICATION_EXCHANGE_NAME_PREFIX + userPrincipal.getId();
    rabbitAdmin.declareExchange(new DirectExchange(exchangeName, false, false));
    rabbitAdmin.declareBinding(
        new Binding(
            queueName,
            Binding.DestinationType.QUEUE,
            exchangeName,
            userPrincipal.getId().toString(),
            null));

    return ResponseEntity.ok(
        ResponseDataAPI.builder().status(CommonConstant.SUCCESS).data(queueName).build());
  }

  private AuthProvider getProvider(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      bearerToken = bearerToken.substring(7);
    }
    OauthAccessToken oauthAccessToken = tokenProvider.getOauthAccessTokenFromToken(bearerToken);
    return oauthAccessToken.getProvider();
  }
}