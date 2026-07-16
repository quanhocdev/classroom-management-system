package com.example.classroom.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
             * Dùng Session cho Thymeleaf page
             * + JWT cho REST API
             */
            .csrf(csrf -> csrf.disable())

               /*
     * Spring tự lưu Authentication vào HttpSession
     */
    .securityContext(context ->
            context.requireExplicitSave(false)
    )


            /*
             * Cho phép Spring tạo session
             *
             * Login xong:
             * SecurityContext
             *       |
             *       ↓
             * HttpSession (JSESSIONID)
             */
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.IF_REQUIRED
                    )
            )



            /*
             * Phân quyền
             */
            .authorizeHttpRequests(auth -> auth



                    /*
                     * Public
                     */
                    .requestMatchers(
                            "/",
                            "/trang-chu",
                            "/auth/**",
                            "/api/auth/**",
                            "/favicon.ico",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    )
                    .permitAll()



                    /*
                     * ==========================
                     * WEB PAGE (SESSION)
                     * ==========================
                     */

                    .requestMatchers(
                            "/admin/**"
                    )
                    .hasRole("ADMIN")


                    .requestMatchers(
                            "/teacher/**"
                    )
                    .hasRole("TEACHER")


                    .requestMatchers(
                            "/student/**"
                    )
                    .hasRole("STUDENT")




                    /*
                     * ==========================
                     * REST API (JWT)
                     * ==========================
                     */


                    .requestMatchers(
                            "/api/admin/**"
                    )
                    .hasAuthority("SCOPE_ADMIN")



                    .requestMatchers(
                            "/api/teacher/**"
                    )
                    .hasAuthority("SCOPE_TEACHER")



                    .requestMatchers(
                            "/api/student/**"
                    )
                    .hasAuthority("SCOPE_STUDENT")




                    /*
                     * Các request còn lại
                     */
                    .anyRequest()
                    .authenticated()

            )




            /*
             * OAuth2 Resource Server
             *
             * Spring tự xử lý:
             *
             * BearerTokenAuthenticationFilter
             * JwtAuthenticationProvider
             * JwtDecoder
             *
             * Không custom filter
             */
            .oauth2ResourceServer(oauth2 -> oauth2

                    .jwt(Customizer.withDefaults())


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