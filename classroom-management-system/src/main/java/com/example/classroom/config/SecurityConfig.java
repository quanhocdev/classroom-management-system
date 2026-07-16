package com.example.classroom.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;

import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


        http

            /*
             * JWT + REST API
             * Không dùng CSRF vì không dùng Session
             */
            .csrf(csrf -> csrf.disable())


            /*
             * JWT là Stateless
             * Server không lưu trạng thái đăng nhập
             */
            .sessionManagement(session -> 
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )


            /*
             * Phân quyền
             */
            .authorizeHttpRequests(auth -> auth


                    /*
                     * Public API
                     */
                    .requestMatchers(
                            "/",
                            "/trang-chu",
                            "/api/auth/login",
                            "/api/auth/register",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    )
                    .permitAll()



                    /*
                     * Admin
                     */
                    .requestMatchers(
                            "/api/admin/**",
                            "/admin/**"
                    )
                    .hasRole("ADMIN")



                    /*
                     * Teacher
                     */
                    .requestMatchers(
                            "/api/teacher/**",
                            "/teacher/**"
                    )
                    .hasRole("TEACHER")



                    /*
                     * Student
                     */
                    .requestMatchers(
                            "/api/student/**",
                            "/student/**"
                    )
                    .hasRole("STUDENT")



                    /*
                     * Những API còn lại
                     */
                    .anyRequest()
                    .authenticated()

            )



            /*
             * JWT Authentication
             *
             * Spring tự dùng:
             * - BearerTokenAuthenticationFilter
             * - JwtDecoder
             * - JwtAuthenticationProvider
             *
             * Không tự viết JwtFilter
             */
            .oauth2ResourceServer(oauth2 -> oauth2

                    .jwt(Customizer.withDefaults())


                    /*
                     * 401 Unauthorized
                     */
                    .authenticationEntryPoint(
                            new BearerTokenAuthenticationEntryPoint()
                    )

            )



            /*
             * 403 Forbidden
             */
            .exceptionHandling(exception -> exception

                    .accessDeniedHandler(
                            new BearerTokenAccessDeniedHandler()
                    )

            );



        return http.build();

    }

}