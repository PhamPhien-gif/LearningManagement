package com.example.learning_management.material;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaterialRepository extends JpaRepository<Material,UUID> ,JpaSpecificationExecutor<Material>  {
    
}
