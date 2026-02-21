package com.example.learning_management.token;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, UUID> {
        @Query(value = """
                        Select t from Token t
                                where t.user.id = :userId
                                and t.expired = false and t.revoked=false """)
        List<Token> findAllValidTokenByUser(UUID userId);

        Optional<Token> findByToken(String token);

        @EntityGraph(attributePaths = { "user" })
        List<Token> findAllTokensByUserId(UUID userId);

        @EntityGraph(attributePaths = { "user" })
        List<Token> findAllTokensByUserEmail(String email);
}
