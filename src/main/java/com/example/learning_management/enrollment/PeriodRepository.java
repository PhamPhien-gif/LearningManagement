package com.example.learning_management.enrollment;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period,UUID>{
    boolean existsById(UUID id);
}
