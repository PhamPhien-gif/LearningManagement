package com.example.learning_management.course;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {

    @EntityGraph(attributePaths = { "instructor", "subject" })
    Page<Course> findAllWithSubjectAndInstructorBy(Specification<Course> spe, Pageable pageable);

    @EntityGraph(attributePaths = { "instructor", "subject", "materials" })
    Optional<Course> findWithInstructorAndSubjectAndMaterialById(UUID id);

    boolean existsByIdAndInstructorId(UUID id, UUID instructorId);

    boolean existsById(UUID id);

    @Modifying
    @Query("Update Course c set c.currentStudents = c.currentStudents + 1 " +
            "where c.currentStudents < c.maxStudents and c.id = :courseId")
    int incrementCourseEnrollment(@Param("courseId") UUID courseId);
}
