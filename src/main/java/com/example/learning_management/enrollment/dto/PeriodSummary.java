package com.example.learning_management.enrollment.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PeriodSummary {
    private UUID id;
    private String name;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
}
