package com.chithanh.italk.security.endpoint;

import com.chithanh.italk.common.common.CommonFunction;
import com.chithanh.italk.common.constant.MessageConstant;
import com.chithanh.italk.common.payload.general.ResponseDataAPI;
import com.chithanh.italk.common.payload.response.ErrorResponse;
import com.chithanh.italk.security.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e)
            throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.UNAUTHORIZED);
        ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
        LogUtils.error(
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURL().toString(),
                e.getMessage());
        httpServletResponse
                .getWriter()
                .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
}