package com.example.classroom.controller.account;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.classroom.dto.account.LoginResponseDTO;
import com.example.classroom.dto.account.LocalLoginRequestDTO;
import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.dto.account.RegisterResponseDTO;
import com.example.classroom.model.Users;
import com.example.classroom.service.account.AuthService;
import com.example.classroom.service.account.JwtService;
  import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthApiController(
            AuthService authService,
            JwtService jwtService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * API ĐĂNG KÝ
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO request
    ) {
        try {
            RegisterResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    /**
     * API ĐĂNG NHẬP
     * Trả về accessToken qua cả JSON Body (cho Android) lẫn Cookie (cho Web)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LocalLoginRequestDTO request
    ) {
        try {
            // 1. Xác thực người dùng qua AuthService để lấy thông tin Users
            Users user = authService.login(request);

            // 2. Sử dụng đúng phương thức generateAccessToken từ JwtService của bạn
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // 3. Cấu hình Cookie "accessToken" sử dụng thời gian sống từ JwtService
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false) // Set thành true khi chạy thực tế trên môi trường HTTPS
                    .path("/")
                    .maxAge(jwtService.getAccessCookieMaxAgeInSeconds()) // Đồng bộ maxAge từ JwtService
                    .sameSite("Lax")
                    .build();

            // 4. Cấu hình Cookie "refreshToken" sử dụng thời gian sống từ JwtService
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(jwtService.getRefreshCookieMaxAgeInSeconds()) // Đồng bộ maxAge từ JwtService
                    .sameSite("Strict")
                    .build();

            // 5. Trả về JSON Body chứa Access Token cho ứng dụng di động Android
            LoginResponseDTO responseBody = new LoginResponseDTO(
                    accessToken,
                    user.getUsername(),
                    user.getRole().name(),
                    "Đăng nhập thành công"
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(responseBody);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }

    /**
     * API REFRESH TOKEN
     * Nhận Refresh Token từ Cookie, xác thực và cấp phát lại cặp Token mới
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        try {
            // 1. Trích xuất Refresh Token từ Cookie gửi lên
            String refreshToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            // 2. Gọi AuthService để giải mã, validate và lấy thông tin Users
            Users user = authService.refresh(refreshToken);

            // 3. Tạo lại cặp Token mới bằng các hàm tương ứng của JwtService
            String newAccessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);

            // 4. Cập nhật Cookie mới cho Trình duyệt
            ResponseCookie newAccessCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(jwtService.getAccessCookieMaxAgeInSeconds())
                    .sameSite("Lax")
                    .build();

            ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(jwtService.getRefreshCookieMaxAgeInSeconds())
                    .sameSite("Strict")
                    .build();

            LoginResponseDTO responseBody = new LoginResponseDTO(
                    newAccessToken,
                    user.getUsername(),
                    user.getRole().name(),
                    "Làm mới token thành công"
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                    .body(responseBody);

        } catch (RuntimeException e) {
            // Nếu Refresh thất bại (hết hạn, giả mạo) -> Xóa toàn bộ Cookie để yêu cầu đăng nhập lại sạch sẽ
            ResponseCookie cleanAccessCookie = ResponseCookie.from("accessToken", "").path("/").maxAge(0).build();
            ResponseCookie cleanRefreshCookie = ResponseCookie.from("refreshToken", "").path("/").maxAge(0).build();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header(HttpHeaders.SET_COOKIE, cleanAccessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                    .body(errorResponse);
        }
    }
  
/**
     * API CHECK THÔNG TIN USER HIỆN TẠI
     * Trả về thông tin người dùng dựa trên JWT giải mã từ Cookie.
     * Nếu chưa đăng nhập, trả về trạng thái isLoggedIn = false với mã 200 OK để tránh lỗi đỏ Console ở trang Public.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // 1. Nếu chưa đăng nhập hoặc token không hợp lệ -> Trả về cấu hình trống an toàn (200 OK)
        if (authentication == null || !authentication.isAuthenticated() || !(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return ResponseEntity.ok(Map.of(
                    "isLoggedIn", false,
                    "message", "Chưa đăng nhập hoặc token đã hết hạn"
            ));
        }

        // 2. Lấy username (subject) từ JWT
        String username = jwtAuth.getName();

        // 3. Lấy role từ danh sách Quyền hạn (Authorities) đã được convert ở SecurityConfig
        String role = jwtAuth.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", "")) // Cắt chữ ROLE_ (ADMIN, TEACHER, STUDENT)
                .findFirst()
                .orElse("STUDENT");

        // 4. Trả về thông tin đăng nhập thành công cho Frontend
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("isLoggedIn", true);
        userInfo.put("username", username);
        userInfo.put("role", role);

        return ResponseEntity.ok(userInfo);
    }
}