package com.example.classroom.controller.subject.admin;
import com.example.classroom.dto.subject.AdminSubjectRequestDTO;
import com.example.classroom.service.subject.AdminSubjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/subjects")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;


    public AdminSubjectController(
            AdminSubjectService adminSubjectService
    ) {
        this.adminSubjectService = adminSubjectService;
    }

    @GetMapping("/mon-hoc/tao")
public String showCreateSubjectPage() {
    return "admin/mon-hoc/tao-mon-hoc";
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