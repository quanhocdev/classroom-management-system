package com.example.classroom.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.classroom.dto.account.GoogleLoginRequestDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.repository.UserRepository;
import com.example.classroom.security.FirebaseService;
import com.example.classroom.security.JwtService;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtService jwtService;


    @Autowired
    private FirebaseService firebaseService;



    public LoginResponseDTO login(LocalLoginRequestDTO request) {

        return null;
    }



    public LoginResponseDTO googleLogin(GoogleLoginRequestDTO request) {

        return null;
    }

}