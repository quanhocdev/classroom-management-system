package com.example.classroom.service.subject;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.classroom.dto.subject.AdminSubjectRequestDTO;
import com.example.classroom.dto.subject.AdminSubjectResponseDTO;
import com.example.classroom.mapper.subject.SubjectMapper;
import com.example.classroom.model.Subject;
import com.example.classroom.model.enums.SubjectStatus;
import com.example.classroom.repository.subject.SubjectRepository;
import com.example.classroom.service.cloudinary.CloudinaryService;
import com.example.classroom.service.cloudinary.UploadResult;


@Service
public class AdminSubjectService {


    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper;

    private final CloudinaryService cloudinaryService;



    public AdminSubjectService(
            SubjectRepository subjectRepository,
            SubjectMapper subjectMapper,
            CloudinaryService cloudinaryService
    ) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.cloudinaryService = cloudinaryService;
    }



    public AdminSubjectResponseDTO createSubject(
        AdminSubjectRequestDTO request,
        MultipartFile image
) {
    if(subjectRepository.existsByCode(request.getCode())) {
        throw new RuntimeException("Subject code already exists");
    }

    // Dùng mapper để chuyển từ Request sang Entity gọn gàng hơn
    Subject subject = subjectMapper.toEntity(request);

    // Xử lý logic nghiệp vụ riêng biệt (upload ảnh)
    if(image != null && !image.isEmpty()) {
        UploadResult result = cloudinaryService.uploadImage(image, "classroom-management/subjects");
        subject.setImageUrl(result.getUrl());
        subject.setImagePublicId(result.getPublicId());
    }

    Subject saved = subjectRepository.save(subject);

    return subjectMapper.toAdminResponseDTO(saved);
}

    public List<AdminSubjectResponseDTO> getAllSubjects() {

        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toAdminResponseDTO)
                .toList();

    }

}