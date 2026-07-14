package com.example.classroom.security;


import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class LoginFailureHandler 
        implements AuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception
    ) throws IOException, ServletException {



        String message;



        if(exception instanceof BadCredentialsException) {


            message =
                "Sai username hoặc password";


        }
        else if(exception instanceof LockedException) {


            message =
                "Tài khoản đã bị khóa";


        }
        else if(exception instanceof DisabledException) {


            message =
                "Tài khoản không hoạt động";


        }
        else {


            message =
                "Đăng nhập thất bại";


        }



        request
            .getSession()
            .setAttribute(
                    "loginError",
                    message
            );



        response.sendRedirect(
                "/auth/login?error"
        );

    }

}