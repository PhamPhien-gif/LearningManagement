package com.example.learning_management.material;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository extends JpaRepository<Material, UUID>, JpaSpecificationExecutor<Material> {

    @Modifying
    @Query("Delete from Material m " +
            "where m.id = :id " +
            "and exists ( select 1 from Course c " +
            "where c.instructor.id = :instructorId " +
            "and c.id = m.course.id)")
    int deleteByIdAndInstructorId(@Param("id") UUID id, @Param("instructorId") UUID instructorId);

    @Query("Select m from Material m " +
            "where m.id = :id and m.course.instructor.id = :instructorId")
    Optional<Material> findByIdAndInstructorId(@Param("id") UUID id, @Param("instructorId") UUID instructorId);

    @EntityGraph(attributePaths = { "course" })
    Optional<Material> findWithCourseById(UUID id);
}
