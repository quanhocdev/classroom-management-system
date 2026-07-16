package com.example.classroom.service.account;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

import org.springframework.stereotype.Service;


import com.example.classroom.model.Users;



@Service
public class JwtService {



    private final JwtEncoder jwtEncoder;



    @Value("${jwt.expiration}")
    private long jwtExpiration;



    public JwtService(
            JwtEncoder jwtEncoder
    ) {

        this.jwtEncoder = jwtEncoder;

    }




    public String generateToken(
            Users user
    ) {


        Instant now = Instant.now();



        JwtClaimsSet claims = JwtClaimsSet.builder()

                /*
                 * Subject mặc định của JWT
                 */
                .subject(
                        user.getUsername()
                )


                /*
                 * Thời gian tạo
                 */
                .issuedAt(
                        now
                )


                /*
                 * Thời gian hết hạn
                 */
                .expiresAt(
                        now.plus(
                                jwtExpiration,
                                ChronoUnit.MILLIS
                        )
                )


                /*
                 * Custom claims
                 */
                .claim(
                        "userId",
                        user.getId()
                )


                .claim(
    "scope",
    "ROLE_" + user.getRole().name()
)


                .claim(
                        "email",
                        user.getEmail()
                )


                .build();



        return jwtEncoder.encode(

                JwtEncoderParameters.from(

                        JwsHeader.with(
                                org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256
                        )
                        .build(),

                        claims

                )

        )
        .getTokenValue();

    }

}