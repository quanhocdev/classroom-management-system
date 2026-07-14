package com.example.classroom.repository;

import com.example.classroom.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(Long id);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByFirebaseUid(String firebaseUid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);

}