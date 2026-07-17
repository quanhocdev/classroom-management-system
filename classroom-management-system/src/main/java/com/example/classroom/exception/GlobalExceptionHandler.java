package com.example.classroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Đánh dấu đây là bộ bắt lỗi tập trung cho toàn bộ Controller
public class GlobalExceptionHandler {

    // Bất kỳ API nào ném ra RuntimeException thì hàm này sẽ tự động đứng ra xử lý
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // Trả về HTTP 400 Bad Request
                .body(errorResponse);
    }
}