package com.example.learning_management.shared;

import com.example.learning_management.config.ErrorCode;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
