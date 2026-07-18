package com.example.classroom.service.account.admin;

import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.model.enums.UserRole;
import com.example.classroom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Lấy danh sách theo Role bất kỳ và tự động map sang DTO
    public List<UserResponseDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }
}