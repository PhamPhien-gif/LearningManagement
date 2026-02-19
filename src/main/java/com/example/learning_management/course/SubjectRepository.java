package com.example.learning_management.course;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, UUID>{
    Page<Subject> findAll(Pageable pageable);
    boolean existsById(UUID id);
}
