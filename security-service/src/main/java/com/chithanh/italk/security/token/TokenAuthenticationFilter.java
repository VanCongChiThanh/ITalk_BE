package com.chithanh.italk.security.token;


import com.chithanh.italk.common.common.CommonFunction;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.exception.ForbiddenException;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.payload.response.ErrorResponse;
import com.chithanh.italk.security.domain.UserPrincipal;
import com.chithanh.italk.security.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

  @Autowired private TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt)) {
        UserDetails userDetails = UserPrincipal.create(tokenProvider.getUserFromToken(jwt));
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    } catch (ForbiddenException ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    } catch (Exception ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.INTERNAL_SERVER_ERROR);
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}