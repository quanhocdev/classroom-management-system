package com.example.classroom.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.classroom.dto.account.GoogleLoginRequestDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.service.account.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LocalLoginRequestDTO request
    ) {

        return ResponseEntity.ok(
            authService.login(request)
        );

    }



    @PostMapping("/google")
    public ResponseEntity<LoginResponseDTO> googleLogin(
            @RequestBody GoogleLoginRequestDTO request
    ) {

        return ResponseEntity.ok(
            authService.googleLogin(request)
        );

    }

}