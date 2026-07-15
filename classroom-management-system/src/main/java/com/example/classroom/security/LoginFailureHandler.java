package com.example.classroom.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String message;

        if (exception instanceof BadCredentialsException) {
            message = "Sai username hoặc password";
        } else if (exception instanceof LockedException) {
            message = "Tài khoản đã bị khóa";
        } else if (exception instanceof DisabledException) {
            message = "Tài khoản không hoạt động";
        } else {
            message = "Đăng nhập thất bại";
        }

        // 1. Thiết lập Header trả về kiểu JSON và mã lỗi 401 Unauthorized
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. Trả chuỗi JSON chứa thông báo lỗi cho JS đọc
        String jsonResponse = String.format("{\"message\":\"%s\"}", message);
        response.getWriter().write(jsonResponse);
    }
}