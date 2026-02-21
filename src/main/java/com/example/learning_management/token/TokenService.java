package com.example.learning_management.token;

import org.springframework.stereotype.Service;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    
    public void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token->{
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void revokeAllUserTokens(String userEmail){
        var validUserTokens = tokenRepository.findAllTokensByUserEmail(userEmail);
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token->{
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void saveUserTokens(User user, String jwtToken, String refreshToken){
        saveRefreshToken(user, refreshToken);
        saveAccessToken(user, jwtToken);
    }

    private void saveRefreshToken(User user, String refreshToken){
        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN);
    }

    private void saveAccessToken(User user, String jwtToken){
        saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN);
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType){
        var token = Token.builder()
                        .user(user)
                        .token(jwtToken)
                        .tokenType(tokenType)
                        .expired(false)
                        .revoked(false)
                        .build();
        tokenRepository.save(token);
    }
}
