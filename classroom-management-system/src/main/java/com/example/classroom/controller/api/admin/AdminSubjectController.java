package com.example.classroom.controller.api.admin;
import com.example.classroom.dto.subject.AdminSubjectRequestDTO;
import com.example.classroom.dto.subject.AdminSubjectResponseDTO;
import com.example.classroom.service.subject.AdminSubjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/admin/subjects")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;


    public AdminSubjectController(
            AdminSubjectService adminSubjectService
    ) {
        this.adminSubjectService = adminSubjectService;
    }

    @GetMapping
    public ResponseEntity<List<AdminSubjectResponseDTO>> getAllSubjects() {
        // Hàm getAllSubjects() bạn đã viết sẵn ở Service rồi, giờ chỉ cần gọi ra thôi
        return ResponseEntity.ok(adminSubjectService.getAllSubjects());
 
    }
 
@PostMapping(consumes = "multipart/form-data")
public ResponseEntity<?> createSubject(

        @RequestPart("data") AdminSubjectRequestDTO request,

        @RequestPart(value = "image", required = false)
        MultipartFile image

){

    return ResponseEntity.ok(
        adminSubjectService.createSubject(request, image)
    );

}

}