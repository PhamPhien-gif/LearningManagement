package com.example.learning_management.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>{
    Optional<User> findByEmail(String email);
    boolean existsByIdAndRole(UUID id, Role role);

    @EntityGraph(attributePaths = {"tokens"})
    Optional<User> findWithTokensById(UUID id);

    @EntityGraph(attributePaths = {"tokens"})
    Optional<User> findWithTokensByEmail(String email);
}
