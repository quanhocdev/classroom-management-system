package com.example.classroom.mapper.subject;

import org.springframework.stereotype.Component;
import com.example.classroom.dto.subject.AdminSubjectRequestDTO;
import com.example.classroom.dto.subject.AdminSubjectResponseDTO;
import com.example.classroom.model.Subject;
import com.example.classroom.model.enums.SubjectStatus;

@Component
public class SubjectMapper {

    // Chuyển từ Request DTO sang Entity
    public Subject toEntity(AdminSubjectRequestDTO request) {
        if (request == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setCode(request.getCode());
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setStatus(SubjectStatus.ACTIVE); 
        return subject;
    }

    // Chuyển từ Entity sang Response DTO
    public AdminSubjectResponseDTO toAdminResponseDTO(Subject subject) {
        if (subject == null) {
            return null;
        }
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