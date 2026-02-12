package com.example.learning_management.config;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_CREDENTIALS("AUTH_001", "Authentication Failed", "Invalid email or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER_001", "User Not Found", "The requested user does not exist", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED("AUTH_002", "Token Expired", "Your session has expired. Please login again", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH_003", "Access Denied", "You do not have permission to access this resource", HttpStatus.FORBIDDEN);

    private final String code;
    private final String title;
    private final String message;
    private final HttpStatus status;
}
