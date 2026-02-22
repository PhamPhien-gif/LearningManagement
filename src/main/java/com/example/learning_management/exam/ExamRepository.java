package com.example.learning_management.exam;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, UUID>{
    
    @EntityGraph(attributePaths = {"course"})
    Optional<Exam> findWithCourseByIdAndCourseId(UUID id, UUID courseId);
}
