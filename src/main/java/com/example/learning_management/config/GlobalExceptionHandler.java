package com.example.learning_management.config;

import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.learning_management.shared.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ProblemDetail> buildProblemDetail(ErrorCode errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getStatus(),
                errorCode.getMessage());
        problemDetail.setTitle(errorCode.getTitle());
        problemDetail.setProperty("errorCode", errorCode.getCode());
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(errorCode.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredential(BadCredentialsException ex){
        logger.error("Authentication failed ", ex);
        return buildProblemDetail(ErrorCode.INVALID_CREDENTIALS);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UsernameNotFoundException ex){
        logger.error("User not found", ex);
        return buildProblemDetail(ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(AppException ex){
        logger.error(ex.getMessage(), ex);
        return buildProblemDetail(ex.getErrorCode());
    }

    //ValidationException
    @Override
    public ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ){
        String detail = ex.getAllErrors().get(0).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(errorCode.getTitle());
        problemDetail.setProperty("errorCode", errorCode.getCode());
        problemDetail.setProperty("timestamp", Instant.now());
        
        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex){
        logger.error("Access Denied", ex);
        return buildProblemDetail(ErrorCode.ACCESS_DENIED);
    }

    // Uncategorized exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handlGenericException(Exception ex){
        logger.error("Unknow error: ", ex);
        return buildProblemDetail(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
