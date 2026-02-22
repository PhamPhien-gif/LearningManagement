package com.example.learning_management.exam;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, UUID>{
    
}
