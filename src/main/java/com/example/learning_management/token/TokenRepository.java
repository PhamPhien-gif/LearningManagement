package com.example.learning_management.token;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query(value = """
    Select t from Token t
            where t.user.id = :userId 
            and t.expired = false and t.revoked=false """)
    List<Token> findAllValidTokenByUser(UUID userId);

    @Query(value = """
    Select t from Token t
            where t.user.email = :email 
            and t.expired = false and t.revoked=false """)
    List<Token> findAllValidTokenByEmail(String email);

    Optional<Token> findByToken(String token);
}
