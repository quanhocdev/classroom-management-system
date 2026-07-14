package com.example.classroom.service.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;

import com.example.classroom.model.Users;
import com.example.classroom.model.enums.UserProvider;
import com.example.classroom.model.enums.UserRole;
import com.example.classroom.model.enums.UserStatus;

import com.example.classroom.repository.UserRepository;



@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;



    public RegisterResponseDTO register(
            RegisterRequestDTO request
    ) {



        if(userRepository
                .existsByUsername(request.getUsername())) {


            throw new RuntimeException(
                    "Username đã tồn tại"
            );

        }



        if(userRepository
                .existsByEmail(request.getEmail())) {


            throw new RuntimeException(
                    "Email đã tồn tại"
            );

        }



        Users user = new Users();



        user.setUsername(
                request.getUsername()
        );


        user.setEmail(
                request.getEmail()
        );



        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );



        /*
         * Người tự đăng ký mặc định là STUDENT
         */
        user.setRole(
                UserRole.STUDENT
        );



        user.setProvider(
                UserProvider.LOCAL
        );



        user.setStatus(
                UserStatus.ACTIVE
        );



        userRepository.save(user);



        return new RegisterResponseDTO(
                "Đăng ký thành công"
        );

    }

}