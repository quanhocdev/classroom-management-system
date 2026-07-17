package com.example.classroom.controller.api.admin;

import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.model.Users;
import com.example.classroom.model.enums.UserRole; // <--- Import Enum vào đây
import com.example.classroom.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminTeacherApiController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminTeacherApiController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserResponseDTO>> getAllTeachers() {
        // Truyền đúng Enum UserRole.TEACHER thay vì chuỗi "TEACHER"
        List<Users> teachers = userRepository.findByRole(UserRole.TEACHER);

        List<UserResponseDTO> responseList = teachers.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}