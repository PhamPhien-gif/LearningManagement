package com.example.learning_management.exam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateExamRequest {
    private String name;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
    private String examUrl;
    private BigDecimal weight;
}
