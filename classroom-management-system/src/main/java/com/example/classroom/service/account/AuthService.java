package com.example.classroom.service.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.classroom.dto.account.GoogleLoginRequestDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.LoginResponseDTO;

import com.example.classroom.model.Users;
import com.example.classroom.repository.UserRepository;

import com.example.classroom.security.FirebaseService;
import com.example.classroom.security.JwtService;

import com.google.firebase.auth.FirebaseToken;



@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtService jwtService;


    @Autowired
    private FirebaseService firebaseService;


    @Autowired
    private PasswordEncoder passwordEncoder;



    // LOGIN LOCAL
    public LoginResponseDTO login(
            LocalLoginRequestDTO request
    ) {


        Users user =
            userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(
                () -> new RuntimeException(
                    "Sai username hoặc password"
                )
            );



        // kiểm tra mật khẩu
        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                "Sai username hoặc password"
            );
        }



        // kiểm tra trạng thái
        if(!user.getStatus().name()
                .equals("ACTIVE")) {

            throw new RuntimeException(
                "Tài khoản đã bị khóa"
            );
        }



        String token =
            jwtService.generateToken(user);



        return createResponse(
                user,
                token
        );

    }





    // LOGIN GOOGLE
    public LoginResponseDTO googleLogin(
            GoogleLoginRequestDTO request
    ) {



        FirebaseToken firebaseToken =
            firebaseService
            .verifyToken(
                request.getFirebaseIdToken()
            );



        String firebaseUid =
                firebaseToken.getUid();



        Users user =
            userRepository
            .findByFirebaseUid(firebaseUid)
            .orElseThrow(
                () -> new RuntimeException(
                    "Tài khoản Google chưa đăng ký"
                )
            );



        if(!user.getStatus().name()
                .equals("ACTIVE")) {

            throw new RuntimeException(
                "Tài khoản đã bị khóa"
            );
        }



        String token =
            jwtService.generateToken(user);



        return createResponse(
                user,
                token
        );

    }





    private LoginResponseDTO createResponse(
            Users user,
            String token
    ) {


        LoginResponseDTO response =
                new LoginResponseDTO();


        response.setToken(token);

        response.setUsername(
                user.getUsername()
        );

        response.setRole(
                user.getRole().name()
        );


        return response;

    }

}