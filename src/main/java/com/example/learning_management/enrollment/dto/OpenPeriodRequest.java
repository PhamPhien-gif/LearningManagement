package com.example.learning_management.enrollment.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenPeriodRequest {
    @NotNull(message = "name must not be null")
    private String name;
    @NotNull(message = "Time Begin must not be null")
    private LocalDateTime timeBegin;
    @NotNull(message = "Time End must not be null")
    private LocalDateTime timeEnd;
}
