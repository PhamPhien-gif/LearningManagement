package com.example.learning_management.exam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamRequest {
    @NotNull(message = "Name must not be null")
    private String name;
    @NotNull(message = "Weight must not be null")
    private BigDecimal weight;
    @NotNull(message = "Time Begin must not be null")
    private LocalDateTime timeBegin;
    @NotNull(message = "Time End must not be null")
    private LocalDateTime timeEnd;
    @NotNull(message = "Exam Url must not be null")
    private String examUrl;
}
