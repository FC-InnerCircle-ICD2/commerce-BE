package com.emotionalcart.common.security;

import com.emotionalcart.common.exception.ErrorCode;
import com.emotionalcart.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws
        IOException,
        ServletException {
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.UNAUTHORIZED.getErrorCode(),
            ErrorCode.UNAUTHORIZED.getMessage()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

}