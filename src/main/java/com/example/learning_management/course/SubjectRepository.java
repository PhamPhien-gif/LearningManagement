package com.example.learning_management.course;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, UUID>{
    
}
