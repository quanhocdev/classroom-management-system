package com.example.classroom.config;

import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            // 1. CHUYỂN SANG STATELESS HOÀN TOÀN (TẮT SESSION)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 2. PHÂN QUYỀN TRUY CẬP
            .authorizeHttpRequests(auth -> auth
                    /*
                     * Public Resources & Auth APIs
                     */
                    .requestMatchers(
                            "/",
                            "/trang-chu",
                            "/auth/**",
                            "/api/auth/**",
                            "/favicon.ico",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    ).permitAll()

                    /*
                     * WEB PAGES (Xác thực bằng Cookie chứa JWT)
                     * Sử dụng hasRole(...) -> Spring sẽ tự kiểm tra "ROLE_ADMIN", "ROLE_TEACHER", ...
                     */
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/teacher/**").hasRole("TEACHER")
                    .requestMatchers("/student/**").hasRole("STUDENT")

                    /*
                     * REST APIs (Xác thực bằng Header hoặc Cookie chứa JWT)
                     */
                    .requestMatchers("/api/admin/**").hasAuthority("SCOPE_ADMIN")
                    .requestMatchers("/api/teacher/**").hasAuthority("SCOPE_TEACHER")
                    .requestMatchers("/api/student/**").hasAuthority("SCOPE_STUDENT")

                    /*
                     * Các request còn lại
                     */
                    .anyRequest().authenticated()
            )

            // 3. OAUTH2 RESOURCE SERVER & CHÍNH SÁCH ĐỌC TOKEN KHÔNG DÙNG CUSTOM FILTER
            .oauth2ResourceServer(oauth2 -> oauth2
                    // Đọc Token tự động từ cả Cookie "accessToken" (cho Web) lẫn Header (cho App Android)
                    .bearerTokenResolver(request -> {
                        // Ưu tiên 1: Lấy Token từ Cookie "accessToken"
                        if (request.getCookies() != null) {
                            for (Cookie cookie : request.getCookies()) {
                                if ("accessToken".equals(cookie.getName())) {
                                    return cookie.getValue();
                                }
                            }
                        }

                        // Ưu tiên 2: Lấy Token từ Header Authorization "Bearer <token>" mặc định
                        DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();
                        return defaultResolver.resolve(request);
                    })
                    // Áp dụng bộ chuyển đổi Authority (phân quyền ROLE_ và SCOPE_)
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    
                    // Xử lý lỗi 401 Unauthorized chuẩn xác để Client (JS trên Web / App Android) nhận biết và gọi API Refresh ngầm
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
            )

            // 4. XỬ LÝ LỖI PHÂN QUYỀN (403 FORBIDDEN)
            .exceptionHandling(exception -> exception
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            );

        return http.build();
    }

    /**
     * Đồng bộ hóa cơ chế Phân quyền (Granted Authorities):
     * Vừa tạo quyền "SCOPE_xxx" (dành cho các endpoint REST API),
     * Vừa tạo quyền "ROLE_xxx" (dành cho các endpoint Web Page sử dụng hasRole).
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        // Giữ nguyên tiền tố SCOPE_ cho API
        scopeConverter.setAuthoritiesClaimName("scope");
        scopeConverter.setAuthorityPrefix("SCOPE_");

        JwtGrantedAuthoritiesConverter roleConverter = new JwtGrantedAuthoritiesConverter();
        // Tạo thêm tiền tố ROLE_ từ claim "scope" để khớp với hasRole("ADMIN") của Web Page
        roleConverter.setAuthoritiesClaimName("scope");
        roleConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = scopeConverter.convert(jwt);
            authorities.addAll(roleConverter.convert(jwt));
            return authorities;
        });

        return converter;
    }
}