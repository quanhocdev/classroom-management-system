package com.example.classroom.controller.api.admin;

import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.model.Users;
import com.example.classroom.model.enums.UserRole;
import com.example.classroom.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminStudentApiController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminStudentApiController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        // Lấy tất cả user từ Database có role là STUDENT
        List<Users> students = userRepository.findByRole(UserRole.STUDENT);

        // Map sang Response DTO để trả về cho Frontend JS
        List<UserResponseDTO> responseList = students.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}