package com.example.classroom.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
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
    public LoginResponseDTO login(
            @RequestBody LocalLoginRequestDTO request
    ) {
        return authService.login(request);
    }


    @PostMapping("/google")
    public LoginResponseDTO googleLogin(
            @RequestBody GoogleLoginRequestDTO request
    ) {
        return authService.googleLogin(request);
    }
}