package com.example.classroom.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.service.account.AuthService;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        try {
            System.out.println("VÀO REGISTER API");
            RegisterResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Trả về lỗi 400 kèm thông báo "Username đã tồn tại" hoặc "Email đã tồn tại"
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LocalLoginRequestDTO request,
            HttpSession session
    ) {
        try {
            Users user = authService.login(request);

            // Lưu thông tin người dùng vào Session
            session.setAttribute("currentUser", user);

            // Đăng nhập thành công -> Trả về HTTP 200 kèm DTO quyền
            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            user.getUsername(),
                            user.getRole().name(),
                            "Đăng nhập thành công"
                    )
            );
        } catch (RuntimeException e) {
            // Bắt các lỗi "Sai username hoặc password" hoặc "Tài khoản bị khóa"
            // Trả về HTTP 400 Bad Request kèm JSON thông điệp lỗi để Front-end hiển thị
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}