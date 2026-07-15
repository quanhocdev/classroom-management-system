package com.example.classroom.dto.subject;

import java.time.LocalDateTime;

import com.example.classroom.model.enums.SubjectStatus;


public class AdminSubjectResponseDTO {

    private Long id;

    private String code;

    private String name;

    private String description;

    private SubjectStatus status;

    private String imageUrl;

    private LocalDateTime createdAt;



    public AdminSubjectResponseDTO(
            Long id,
            String code,
            String name,
            String description,
            SubjectStatus status,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }



    public Long getId() {
        return id;
    }


    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public SubjectStatus getStatus() {
        return status;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}