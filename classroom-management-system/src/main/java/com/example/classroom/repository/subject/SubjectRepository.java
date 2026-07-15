package com.example.classroom.repository.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.classroom.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByCode(String code);

}