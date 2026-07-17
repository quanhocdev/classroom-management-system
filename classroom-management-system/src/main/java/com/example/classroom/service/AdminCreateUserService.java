package com.example.classroom.service;

import com.example.classroom.dto.account.AdminCreateUserRequestDTO;
import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.model.Users;
import com.example.classroom.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminCreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminCreateUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponseDTO createUserByAdmin(AdminCreateUserRequestDTO request) {
        // Kiểm tra tính hợp lệ dữ liệu trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại trên hệ thống!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }

        // Chuyển đổi DTO -> Entity thông qua Mapper
        Users user = userMapper.toEntity(request);

        // Lưu thông tin vào Database
        Users savedUser = userRepository.save(user);

        // Chuyển đổi Entity đã lưu -> Response DTO để phản hồi lại Client
        return userMapper.toResponseDTO(savedUser);
    }
}