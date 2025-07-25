package com.chithanh.italk.web.advice;


import com.chithanh.italk.common.common.CommonFunction;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.*;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.payload.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.rmi.ServerError;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String SERVER_ERROR_CODE = "ERR.SERVER";
  private static final String ACCESS_DENIED_ERROR = "forbidden_error";

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ResponseDataAPI> notFoundException(
          NotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDataAPI> conflictException(
            ConflictException ex, HttpServletRequest request) {
        ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
        ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
        return new ResponseEntity<>(responseDataAPI, HttpStatus.CONFLICT);
    }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ResponseDataAPI> badRequestException(
          BadRequestException ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ResponseDataAPI> forbiddenException(
          ForbiddenException ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ResponseDataAPI> unauthorizedException(
          UnauthorizedException ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ServerError.class)
  public ResponseEntity<ResponseDataAPI> serverError(ServerError ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
          MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    String msg =
            ex.getName()
                    + " should be of type "
                    + Objects.requireNonNull(ex.getRequiredType()).getName();

    ErrorResponse error = new ErrorResponse(SERVER_ERROR_CODE, msg);
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ResponseDataAPI> maxUploadSizeExceededException(
          MaxUploadSizeExceededException ex, HttpServletRequest request) {
    ErrorResponse error =
            CommonFunction.getExceptionError(MessageConstant.MAXIMUM_UPLOAD_SIZE_EXCEEDED);
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseDataAPI> accessDeniedExceptionHandle(
          AccessDeniedException ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ACCESS_DENIED_ERROR);
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.FORBIDDEN);
  }

  //  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDataAPI> globalExceptionHandler(
          Exception ex, HttpServletRequest request) {
    ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    return new ResponseEntity<>(responseDataAPI, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {
    List<ObjectError> listError = ex.getBindingResult().getAllErrors();
    ObjectError objectError = listError.get(listError.size() - 1);
    String error = CommonFunction.convertToSnakeCase(Objects.requireNonNull(objectError.getCode()));
    String fieldName = CommonFunction.convertToSnakeCase(((FieldError) objectError).getField());
    String resource = CommonFunction.convertToSnakeCase(objectError.getObjectName());

    ErrorResponse errorResponse = CommonFunction.getValidationError(resource, fieldName, error);

    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(errorResponse);

    return new ResponseEntity<>(responseDataAPI, HttpStatus.BAD_REQUEST);
  }
}