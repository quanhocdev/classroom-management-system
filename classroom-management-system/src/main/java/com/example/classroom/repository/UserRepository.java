package com.example.classroom.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.classroom.model.Users;



@Repository
public interface UserRepository 
        extends JpaRepository<Users, Long> {



    Optional<Users> findByUsername(
            String username
    );



    Optional<Users> findByEmail(
            String email
    );



    boolean existsByUsername(
            String username
    );



    boolean existsByEmail(
            String email
    );



}