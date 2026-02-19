package com.example.learning_management.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.config.JwtService;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.token.TokenService;
import com.example.learning_management.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public AuthenticationResponse authentication(AuthenticationRequest request) {

        // Authenticate username (email) and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // if username and password correct, find user
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // generate tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // revoke all tokens before login
        tokenService.revokeAllUserTokens(user);

        // save in database
        tokenService.saveUserTokens(user, jwtToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String authHeader) {
        // check header
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new IllegalArgumentException("Invalid Authorization format");
        }

        final String jwt = authHeader.substring(7);

        // check user
        final String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        var userDetail = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check refreshToken
        if (!jwtService.isRefreshTokenValid(jwt, userDetail)) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        // generate, revoke and save tokens
        var accessToken = jwtService.generateToken(userDetail);
        var refreshToken = jwtService.generateRefreshToken(userDetail);
        tokenService.revokeAllUserTokens(userDetail);
        tokenService.saveUserTokens(userDetail, accessToken, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
