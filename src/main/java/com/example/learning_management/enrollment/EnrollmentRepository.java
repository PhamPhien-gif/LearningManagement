package com.example.learning_management.enrollment;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID>{
    Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    boolean existsByStudentIdAndCourseId(UUID student, UUID courseId);
}
