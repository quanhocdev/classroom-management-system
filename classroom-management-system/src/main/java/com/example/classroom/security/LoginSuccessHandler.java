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
public class LoginSuccessHandler 
        implements AuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {



        String redirectUrl = "/trang-chu";



        for(GrantedAuthority authority 
                : authentication.getAuthorities()) {



            String role = authority.getAuthority();



            if(role.equals("ROLE_ADMIN")) {


                redirectUrl =
                        "/admin/dashboard";


                break;


            }



            if(role.equals("ROLE_TEACHER")) {


                redirectUrl =
                        "/teacher/dashboard";


                break;


            }



            if(role.equals("ROLE_STUDENT")) {


                redirectUrl =
                        "/student/dashboard";


                break;


            }

        }



        response.sendRedirect(
                redirectUrl
        );

    }

}