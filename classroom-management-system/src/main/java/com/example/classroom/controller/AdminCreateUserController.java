package com.example.classroom.controller;

import com.example.classroom.dto.account.AdminCreateUserRequestDTO;
import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.service.AdminCreateUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminCreateUserController {

    private final AdminCreateUserService adminCreateUserService;

    public AdminCreateUserController(AdminCreateUserService adminCreateUserService) {
        this.adminCreateUserService = adminCreateUserService;
    }

    /**
     * API Admin tạo tài khoản người dùng mới (STUDENT hoặc TEACHER)
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) // Tự động trả về HTTP 201 Created khi thành công
    public UserResponseDTO createUserByAdmin(@RequestBody AdminCreateUserRequestDTO request) {
        return adminCreateUserService.createUserByAdmin(request);
    }
}