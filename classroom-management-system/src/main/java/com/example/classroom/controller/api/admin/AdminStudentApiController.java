package com.example.classroom.controller.api.admin;

import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.model.enums.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.classroom.service.account.admin.AdminUserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminStudentApiController {

    private final AdminUserService adminUserService;

    public AdminStudentApiController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(adminUserService.getUsersByRole(UserRole.STUDENT));
    }
}