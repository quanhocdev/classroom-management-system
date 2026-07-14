package com.example.classroom.security;

import java.io.IOException;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomLogoutSuccessHandler 
        implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.Authentication authentication
    ) throws IOException, ServletException {


        response.sendRedirect(
                "/auth/login?logout"
        );

    }

}