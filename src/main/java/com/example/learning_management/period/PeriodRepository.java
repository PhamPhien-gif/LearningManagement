package com.example.learning_management.period;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period,UUID>{
    boolean existsById(UUID id);

    @EntityGraph(attributePaths = {"courses"})
    Optional<Period> findWithCoursesById(UUID id);
}
