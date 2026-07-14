package com.example.classroom.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.classroom.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {


    private final SecretKey secretKey;


    private final long expiration;


    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }



    // Sinh JWT
    public String generateToken(Users user) {


        return Jwts.builder()

                .subject(user.getUsername())

                .claim(
                    "userId",
                    user.getId()
                )

                .claim(
                    "role",
                    user.getRole().name()
                )

                .issuedAt(
                    new Date()
                )

                .expiration(
                    new Date(
                        System.currentTimeMillis()
                        + expiration
                    )
                )

                .signWith(secretKey)

                .compact();

    }



    // Lấy username
    public String extractUsername(String token) {

        return extractClaims(token)
                .getSubject();

    }



    // Lấy userId
    public Long extractUserId(String token) {

        return extractClaims(token)
                .get("userId", Long.class);

    }



    // Lấy role
    public String extractRole(String token) {

        return extractClaims(token)
                .get("role", String.class);

    }



    // Kiểm tra token
    public boolean isValid(String token) {

        try {

            extractClaims(token);

            return true;

        } catch(Exception e) {

            return false;

        }
    }



    private Claims extractClaims(String token) {


        return Jwts.parser()

                .verifyWith(secretKey)

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

}