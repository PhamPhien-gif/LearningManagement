package com.example.learning_management.auth;

import javax.management.RuntimeErrorException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        revokeAllUserTokens(user);
        saveUserTokens(user, jwtToken, refreshToken);
        return AuthenticationResponse.builder()
                                    .accessToken(jwtToken)
                                    .refreshToken(refreshToken)
                                    .build();
    }

    private void revokeAllUserTokens(User user){
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

    private void saveUserTokens(User user, String jwtToken, String refreshToken){
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

    public AuthenticationResponse refreshToken(String authHeader){
        
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            throw new IllegalArgumentException("Invalid Authorization format");
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        if(userEmail == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var userDetail = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var token = tokenRepository.findByToken(jwt).orElse(null);
        final boolean isValidToken;
        if(token == null || token.isExpired() || token.isRevoked() || token.getTokenType() != TokenType.REFRESH_TOKEN){
            isValidToken = false;
        }else{
            isValidToken = true;
        }

        if(!jwtService.isTokenvalid(jwt, userDetail) || !isValidToken){
            throw new BadCredentialsException("Refresh Token is invalid");
        }
        var accessToken = jwtService.generateToken(userDetail);
            var refreshToken = jwtService.generateRefreshToken(userDetail);
            revokeAllUserTokens(userDetail);
            saveUserTokens(userDetail, accessToken, refreshToken);
            return AuthenticationResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .build();
    }

}
