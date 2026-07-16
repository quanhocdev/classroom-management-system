package com.example.classroom.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
public class AuthenticationConfig {


    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;



    public AuthenticationConfig(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

    }




    /*
     * Provider dùng:
     * - UserDetailsService lấy user từ database
     * - PasswordEncoder kiểm tra password BCrypt
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {


        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();


        provider.setUserDetailsService(
                userDetailsService
        );


        provider.setPasswordEncoder(
                passwordEncoder
        );


        return provider;

    }




    /*
     * AuthenticationManager được AuthController gọi khi login
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {


        return configuration.getAuthenticationManager();

    }

}