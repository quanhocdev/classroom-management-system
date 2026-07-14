package com.example.classroom.security;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;


    @Autowired
    private CustomUserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {



        String authHeader =
                request.getHeader("Authorization");


        String token = null;

        String username = null;



        // Lấy JWT từ Header
        if(authHeader != null 
                && authHeader.startsWith("Bearer ")) {


            token = authHeader.substring(7);


            if(jwtService.isValid(token)) {

                username =
                    jwtService.extractUsername(token);

            }
        }



        // Nếu có username và chưa đăng nhập
        if(username != null 
            && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {



            CustomUserDetails userDetails =
                    (CustomUserDetails)
                    userDetailsService
                    .loadUserByUsername(username);



            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );



            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                    .buildDetails(request)
            );



            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        }



        filterChain.doFilter(
                request,
                response
        );

    }

}