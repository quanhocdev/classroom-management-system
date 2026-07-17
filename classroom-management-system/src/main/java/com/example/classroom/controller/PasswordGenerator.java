package com.example.classroom.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Admin_01
        System.out.println(
            encoder.encode("Admin123@")
        );
        // Teacher_01
        System.out.println(
            encoder.encode("Teacher123@")
        );
    }
}
