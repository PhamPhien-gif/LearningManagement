package com.example.learning_management.enrollment;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment>{
    Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    boolean existsByStudentIdAndCourseId(UUID student, UUID courseId);
    boolean existsByIdAndStudentId(UUID id, UUID studentId);
    int deleteByIdAndStudentId(UUID id, UUID studentId);
    int deleteWithRoleRegistrarById(UUID id);
}
