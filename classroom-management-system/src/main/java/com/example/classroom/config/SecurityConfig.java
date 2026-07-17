package com.example.classroom.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

    // =========================================================================
    // FIX LỖI MIME TYPE: Cho phép tải trực tiếp JS, CSS, Images không qua bộ lọc
    // =========================================================================
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
        );
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
                            "/api/auth/**"
                    ).permitAll()

                    /*
                     * WEB PAGES (Xác thực bằng Cookie chứa JWT)
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
                    
                    // XỬ LÝ LỖI 401 THÔNG MINH: Chỉ chặn nếu truy cập trang bảo mật, bỏ qua nếu là trang Public
                    .authenticationEntryPoint((request, response, authException) -> {
                        String uri = request.getRequestURI();
                        
                        // Nếu là trang public hoặc auth APIs, cho phép đi tiếp (coi như Anonymous)
                        if (uri.equals("/") || uri.equals("/trang-chu") || uri.startsWith("/auth/") || uri.startsWith("/api/auth/")) {
                            request.getRequestDispatcher(uri).forward(request, response);
                        } else {
                            // Ngược lại, nếu cố tình vào trang bảo mật (/admin, /api/admin,...) thì mới chặn 401
                            new BearerTokenAuthenticationEntryPoint().commence(request, response, authException);
                        }
                    })
            )
            
            // 4. XỬ LÝ LỖI PHÂN QUYỀN (403 FORBIDDEN)
            .exceptionHandling(exception -> exception
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )

            // =========================================================================
            // 5. CẤU HÌNH LOGOUT (DỌN DẸP COOKIE PHÍA SERVER & TRẢ VỀ 200 OK CHO JS)
            // =========================================================================
            .logout(logout -> logout
                    .logoutUrl("/api/auth/logout")             // Endpoint nhận request logout từ JS
                    .deleteCookies("accessToken")              // Chỉ thị trình duyệt xóa Cookie accessToken
                    .clearAuthentication(true)                 // Xóa thông tin Authentication trong Security Context
                    .logoutSuccessHandler((request, response, authentication) -> {
                        // Trả về HTTP 200 OK để báo cho fetch() phía JS biết là đã logout thành công
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
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
        scopeConverter.setAuthoritiesClaimName("scope");
        scopeConverter.setAuthorityPrefix("SCOPE_");

        JwtGrantedAuthoritiesConverter roleConverter = new JwtGrantedAuthoritiesConverter();
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