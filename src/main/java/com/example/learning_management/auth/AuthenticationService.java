package com.example.learning_management.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.learning_management.config.JwtService;
import com.example.learning_management.token.Token;
import com.example.learning_management.token.TokenRepository;
import com.example.learning_management.token.TokenType;
import com.example.learning_management.user.User;
import com.example.learning_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse authentication(AuthenticationRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user, jwtToken);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                                    .accessToken(jwtToken)
                                    .refreshToken(refreshToken)
                                    .build();
    }

    private void revokeAllUserTokens(User user, String jwtToken){
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

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                        .user(user)
                        .token(jwtToken)
                        .expired(false)
                        .revoked(false)
                        .build();
        tokenRepository.save(token);
    }
}
