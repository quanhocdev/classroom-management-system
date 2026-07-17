package com.example.classroom.service.account;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import com.example.classroom.model.Users;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    // Định nghĩa thời gian sống trực tiếp hoặc lấy từ file application.properties
    // Nếu trong file cấu hình không có, ta sẽ sử dụng giá trị mặc định cực kỳ an toàn
    @Value("${jwt.access-expiration:300000}") // 5 phút = 300,000 ms
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 ngày = 604,800,000 ms
    private long refreshTokenExpiration;

    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Sinh ACCESS TOKEN (Hạn dùng: 5 phút)
     * Chứa đầy đủ thông tin quyền hạn (scope/role), ID, Email để thực hiện giao dịch nhanh.
     */
    public String generateAccessToken(Users user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiration, ChronoUnit.MILLIS))
                // OAuth2 Resource Server đọc claim này để phân quyền (hasRole, hasAuthority)
                .claim("scope", user.getRole().name())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                // Đánh dấu đây là loại access token
                .claim("tokenType", "ACCESS")
                .build();

        return encodeToken(claims);
    }

    /**
     * Sinh REFRESH TOKEN (Hạn dùng: 7 ngày)
     * Token này CHỈ chứa thông tin định danh tối thiểu (Username) và không chứa Roles
     * nhằm bảo mật tối đa nếu chẳng may bị lộ. Chỉ dùng để đổi Access Token mới.
     */
    public String generateRefreshToken(Users user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiration, ChronoUnit.MILLIS))
                // Đánh dấu đây là loại refresh token để tránh kẻ xấu lấy Refresh Token đi gọi API thông thường
                .claim("tokenType", "REFRESH")
                .build();

        return encodeToken(claims);
    }

    /**
     * Giải mã và đọc tên người dùng (Subject) từ một token bất kỳ
     */
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    /**
     * Kiểm tra xem một chuỗi token có phải là Refresh Token hợp lệ hay không
     */
    public boolean isRefreshToken(String token) {
        try {
            var jwt = jwtDecoder.decode(token);
            String tokenType = jwt.getClaimAsString("tokenType");
            return "REFRESH".equals(tokenType) && jwt.getExpiresAt().isAfter(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Trả về thời gian sống cấu hình của Access Token (để Controller thiết lập thời gian sống Cookie đồng bộ)
     * Đơn vị: giây (phục vụ setMaxAge của Cookie)
     */
    public int getAccessCookieMaxAgeInSeconds() {
        // Trả về thời hạn của Cookie accessToken (bạn muốn đặt 10 phút = 600 giây)
        return 600; 
    }

    /**
     * Trả về thời gian sống cấu hình của Refresh Token
     * Đơn vị: giây (phục vụ setMaxAge của Cookie)
     */
    public int getRefreshCookieMaxAgeInSeconds() {
        return (int) (refreshTokenExpiration / 1000);
    }

    /**
     * Hàm phụ trợ mã hóa claims thành chuỗi token JWT hoàn chỉnh
     */
    private String encodeToken(JwtClaimsSet claims) {
        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}