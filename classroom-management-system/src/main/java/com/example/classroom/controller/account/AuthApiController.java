package com.example.classroom.controller.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.service.account.AuthService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api/auth")
public class AuthApiController {



    @Autowired
    private AuthService authService;



    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO request
    ){


            System.out.println("VÀO REGISTER API");


        RegisterResponseDTO response =
                authService.register(request);



        return ResponseEntity.ok(response);

    }




@PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody LocalLoginRequestDTO request,
        HttpSession session
) {


    Users user =
        authService.login(request);



    session.setAttribute(
            "currentUser",
            user
    );


    return ResponseEntity.ok(
            new LoginResponseDTO(
                    user.getUsername(),
                    user.getRole().name(),
                    "Đăng nhập thành công"
            )
    );

}


}