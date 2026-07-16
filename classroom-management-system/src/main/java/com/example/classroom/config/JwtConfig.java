package com.example.classroom.config;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


import com.nimbusds.jose.jwk.source.ImmutableSecret;



@Configuration
public class JwtConfig {


    @Value("${jwt.secret}")
    private String jwtSecret;



    @Bean
    public SecretKey secretKey() {


        return new SecretKeySpec(
                jwtSecret.getBytes(),
                "HmacSHA256"
        );

    }



    @Bean
    public JwtEncoder jwtEncoder(
            SecretKey secretKey
    ) {


        return new NimbusJwtEncoder(
                new ImmutableSecret<>(
                        secretKey
                )
        );

    }



    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey secretKey
    ) {


        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .build();

    }

}