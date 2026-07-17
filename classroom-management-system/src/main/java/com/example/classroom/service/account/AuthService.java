package com.example.classroom.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.classroom.dto.account.AdminCreateUserRequestDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO; // Sử dụng đúng DTO của bạn
import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.model.enums.UserProvider;
import com.example.classroom.model.enums.UserRole;
import com.example.classroom.model.enums.UserStatus;
import com.example.classroom.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService; // Đảm bảo bạn đã khai báo bean JwtService này

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        user.setRole(UserRole.STUDENT);
        user.setProvider(UserProvider.LOCAL);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        return new RegisterResponseDTO("Đăng ký thành công");
    }

    public Users login(LocalLoginRequestDTO request) {
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Tài khoản hoặc mật khẩu không chính xác");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản đã bị khóa hoặc chưa kích hoạt");
        }

        return user;
    }

    public Users refresh(String refreshToken) {
        if (refreshToken == null || !jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token không hợp lệ hoặc đã hết hạn");
        }

        String username = jwtService.extractUsername(refreshToken);

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        return user;
    }
  
}