package com.example.learning_management.course;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {
    
    @Override
    @EntityGraph(attributePaths = {"instructor", "subject"})
    Page<Course> findAll(Specification<Course> spe, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"instructor", "subject", "materials"})
    Optional<Course> findById(UUID id);

    boolean existsByIdAndInstructorId(UUID id, UUID instructorId);
    boolean existsById(UUID id);
}
