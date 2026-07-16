package com.example.classroom.config;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


import com.nimbusds.jose.jwk.source.ImmutableSecret;



@Configuration
public class JwtConfig {


    @Value("${jwt.secret}")
    private String jwtSecret;



    /*
     * Secret key dùng chung:
     * - Encoder ký token
     * - Decoder kiểm tra token
     */
    private SecretKey getSecretKey() {


        return new SecretKeySpec(
                jwtSecret.getBytes(),
                MacAlgorithm.HS256.getName()
        );

    }




    @Bean
    public JwtEncoder jwtEncoder() {


        return new NimbusJwtEncoder(
                new ImmutableSecret<>(
                        getSecretKey()
                )
        );

    }





    @Bean
    public JwtDecoder jwtDecoder() {


        NimbusJwtDecoder jwtDecoder =
                NimbusJwtDecoder
                        .withSecretKey(
                                getSecretKey()
                        )
                        .macAlgorithm(
                                MacAlgorithm.HS256
                        )
                        .build();



        return token -> {

            try {

                return jwtDecoder.decode(token);

            } catch (Exception e) {

                System.out.println(
                        ">>> JWT error: " 
                        + e.getMessage()
                );

                throw e;

            }

        };

    }

}