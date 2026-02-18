package com.example.learning_management.config;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //Validation
    INVALID_INPUT("GEN_001", "Invalid Input", "Invalid Input", HttpStatus.BAD_REQUEST),

    //Auth Errors
    INVALID_CREDENTIALS("AUTH_001", "Authentication Failed", "Invalid email or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER_001", "User Not Found", "The requested user does not exist", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED("AUTH_002", "Token Expired", "Your session has expired. Please login again", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH_003", "Access Denied", "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    TOKEN_INVALID("AUTH_004", "Token Invalid", "Your token is invalid", HttpStatus.UNAUTHORIZED),
    // Course Erors
    REGISTRAR_NOT_FOUND("COURSE_001", "Registrar Not Found", "The specified registrar (ID) does not exist", HttpStatus.NOT_FOUND),
    INSTRUCTOR_NOT_FOUND("COURSE_002", "Instructor Not Found", "The specified instructor (ID) does not exist", HttpStatus.NOT_FOUND),
    SUBJECT_NOT_FOUND("COURSE_003", "Subject Not Found", "The specified subject (ID) is invalid or does not exist", HttpStatus.NOT_FOUND),
    INVALID_COURSE_CAPACITY("COURSE_004", "Invalid Max Students", "Max students must be between 5 and 100", HttpStatus.BAD_REQUEST),
    INVALID_COURSE_TIME("COURSE_005", "Invalid Course Time", "Begin time must be earlier than end time", HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND("COURSE_006", "Course Not Found","The specified course (ID) is invalid or does not exist", HttpStatus.NOT_FOUND),

    //Generic Erors
    UNCATEGORIZED_EXCEPTION("9999", "Uncategorized Error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String title;
    private final String message;
    private final HttpStatus status;
}
