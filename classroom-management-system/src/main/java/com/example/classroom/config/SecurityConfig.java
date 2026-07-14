package com.example.classroom.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


import com.example.classroom.security.LoginFailureHandler;
import com.example.classroom.security.LoginSuccessHandler;
import com.example.classroom.security.CustomLogoutSuccessHandler;


@Configuration
public class SecurityConfig {



    @Autowired
    private LoginSuccessHandler loginSuccessHandler;



    @Autowired
    private LoginFailureHandler loginFailureHandler;



   @Autowired
private CustomLogoutSuccessHandler logoutSuccessHandler;



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
             * Session Authentication
             *
             * Spring Security tự tạo JSESSIONID
             */
            .sessionManagement(session -> session
                    .maximumSessions(1)
            )



            .authorizeHttpRequests(auth -> auth



                    /*
                     * Public
                     */
                    .requestMatchers(

                            "/",
                            "/trang-chu",

                            "/auth/login",
                            "/auth/register",

                            "/css/**",
                            "/js/**",
                            "/images/**"

                    )
                    .permitAll()





                    /*
                     * API register
                     */
                    .requestMatchers(
                            "/api/auth/register"
                    )
                    .permitAll()





                    /*
                     * Admin
                     */
                    .requestMatchers(
                            "/admin/**"
                    )
                    .hasRole("ADMIN")





                    /*
                     * Teacher
                     */
                    .requestMatchers(
                            "/teacher/**"
                    )
                    .hasRole("TEACHER")





                    /*
                     * Student
                     */
                    .requestMatchers(
                            "/student/**"
                    )
                    .hasRole("STUDENT")





                    /*
                     * Các request còn lại
                     * bắt buộc login
                     */
                    .anyRequest()
                    .authenticated()


            )





            /*
             * Form Login
             */
            .formLogin(login -> login



                    .loginPage(
                            "/auth/login"
                    )



                    .loginProcessingUrl(
                            "/login"
                    )



                    .usernameParameter(
                            "username"
                    )



                    .passwordParameter(
                            "password"
                    )



                    .successHandler(
                            loginSuccessHandler
                    )



                    .failureHandler(
                            loginFailureHandler
                    )



                    .permitAll()

            )





            /*
             * Logout
             */
            .logout(logout -> logout



                    .logoutUrl(
                            "/logout"
                    )



                    .logoutSuccessHandler(
                            logoutSuccessHandler
                    )



                    .invalidateHttpSession(true)



                    .clearAuthentication(true)



                    .deleteCookies(
                            "JSESSIONID"
                    )



                    .permitAll()

            );





        return http.build();

    }

}