package com.example.classroom.service.account;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
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


                .subject(
                        user.getUsername()
                )


                .issuedAt(
                        now
                )


                .expiresAt(
                        now.plus(
                                jwtExpiration,
                                ChronoUnit.MILLIS
                        )
                )


                /*
                 * OAuth2 Resource Server đọc claim này
                 *
                 * ADMIN
                 *  ↓
                 * SCOPE_ADMIN
                 */
                .claim(
                        "scope",
                        user.getRole().name()
                )


                .claim(
                        "userId",
                        user.getId()
                )


                .claim(
                        "email",
                        user.getEmail()
                )


                .build();



        return jwtEncoder.encode(

        JwtEncoderParameters.from(

                JwsHeader.with(
                        MacAlgorithm.HS256
                )
                .build(),

                claims

        )

)
.getTokenValue();

    }

}