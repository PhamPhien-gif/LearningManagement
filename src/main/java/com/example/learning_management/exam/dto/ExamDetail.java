package com.example.learning_management.exam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetail {
    private UUID id;
    private String name;
    private BigDecimal weight;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
    private String examUrl;
    // private UUID courseId;

}
