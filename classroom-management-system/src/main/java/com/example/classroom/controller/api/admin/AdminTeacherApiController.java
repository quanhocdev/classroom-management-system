package com.example.classroom.controller.api.admin;

import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.model.enums.UserRole; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.classroom.service.account.admin.AdminUserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminTeacherApiController {

    private final AdminUserService adminUserService;


    public AdminTeacherApiController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(adminUserService.getUsersByRole(UserRole.TEACHER));
    }
}