package com.example.classroom.controller.account;


import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;


import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;

import com.example.classroom.model.Users;

import com.example.classroom.security.CustomUserDetails;

import com.example.classroom.service.account.AuthService;
import com.example.classroom.service.account.JwtService;



@RestController
@RequestMapping("/api/auth")
public class AuthApiController {



    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;



    public AuthApiController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }





    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO request
    ) {


        try {


            RegisterResponseDTO response =
                    authService.register(request);


            return ResponseEntity.ok(response);


        } catch (RuntimeException e) {


            Map<String, String> errorResponse =
                    new HashMap<>();


            errorResponse.put(
                    "message",
                    e.getMessage()
            );


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);

        }

    }





    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LocalLoginRequestDTO request
    ) {


        try {


            Authentication authentication =
                    authenticationManager.authenticate(

                            new UsernamePasswordAuthenticationToken(

                                    request.getUsername(),

                                    request.getPassword()

                            )

                    );



            CustomUserDetails userDetails =
                    (CustomUserDetails)
                            authentication.getPrincipal();



            Users user =
                    userDetails.getUser();



            String token =
                    jwtService.generateToken(user);



            return ResponseEntity.ok(

                    new LoginResponseDTO(

                            token,

                            user.getUsername(),

                            user.getRole().name(),

                            "Đăng nhập thành công"

                    )

            );



        } catch (RuntimeException e) {


            Map<String, String> errorResponse =
                    new HashMap<>();


            errorResponse.put(
                    "message",
                    e.getMessage()
            );


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);

        }

    }

}