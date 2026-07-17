package com.example.classroom.mapper;

import com.example.classroom.dto.account.AdminCreateUserRequestDTO;
import com.example.classroom.dto.account.UserResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.model.enums.UserProvider;
import com.example.classroom.model.enums.UserRole;
import com.example.classroom.model.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Chuyển đổi Request DTO từ Admin thành Entity Users
    public Users toEntity(AdminCreateUserRequestDTO request) {
        if (request == null) {
            return null;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Thiết lập trạng thái ban đầu của tài khoản được tạo bởi Admin
        user.setProvider(UserProvider.LOCAL);
        user.setStatus(UserStatus.ACTIVE);

        // Map Role từ String sang Enum tương ứng
        try {
            UserRole targetRole = UserRole.valueOf(request.getRole().toUpperCase());
            if (targetRole == UserRole.ADMIN) {
                throw new RuntimeException("Không được phép tạo tài khoản có quyền ADMIN.");
            }
            user.setRole(targetRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Vai trò (Role) không hợp lệ.");
        }

        return user;
    }

    // Chuyển đổi Entity Users sang Response DTO an toàn cho Admin
    public UserResponseDTO toResponseDTO(Users user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getProvider().name(),
                user.getStatus().name(),
                user.getCreatedAt()
        );
    }
}