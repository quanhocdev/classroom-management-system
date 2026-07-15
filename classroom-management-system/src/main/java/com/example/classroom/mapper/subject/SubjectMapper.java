package com.example.classroom.mapper.subject;

import org.springframework.stereotype.Component;

import com.example.classroom.dto.subject.AdminSubjectResponseDTO;
import com.example.classroom.model.Subject;

@Component
public class SubjectMapper {


    public AdminSubjectResponseDTO toAdminResponseDTO(Subject subject) {

        return new AdminSubjectResponseDTO(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getDescription(),
                subject.getStatus(),
                subject.getImageUrl(),
                subject.getCreatedAt()
        );
    }

}