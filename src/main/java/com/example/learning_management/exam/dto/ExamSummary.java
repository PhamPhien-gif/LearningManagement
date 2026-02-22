package com.example.learning_management.exam.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamSummary {
    private UUID id;
    private String name;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
}
