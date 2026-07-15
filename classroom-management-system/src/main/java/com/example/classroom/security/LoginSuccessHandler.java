package com.example.classroom.security;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1. Cấu hình kiểu dữ liệu trả về là JSON thay vì Redirect trang
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String userRole = "";

        // 2. Lấy ra Quyền (Role) của người dùng
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN") || role.equals("ADMIN")) {
                userRole = "ADMIN";
                break;
            }
            if (role.equals("ROLE_TEACHER") || role.equals("TEACHER")) {
                userRole = "TEACHER";
                break;
            }
            if (role.equals("ROLE_STUDENT") || role.equals("STUDENT")) {
                userRole = "STUDENT";
                break;
            }
        }

        // 3. Ghi chuỗi JSON trả về cho Front-end đọc
        String jsonResponse = String.format("{\"role\":\"%s\",\"message\":\"Đăng nhập thành công\"}", userRole);
        response.getWriter().write(jsonResponse);
    }
}