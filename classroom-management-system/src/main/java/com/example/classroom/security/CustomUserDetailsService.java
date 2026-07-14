package com.example.classroom.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import com.example.classroom.model.Users;
import com.example.classroom.repository.UserRepository;



@Service
public class CustomUserDetailsService 
        implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(
            String username
    ) {


        Users user =
            userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new RuntimeException(
                    "Không tìm thấy tài khoản"
                )
            );


        return new CustomUserDetails(user);

    }

}