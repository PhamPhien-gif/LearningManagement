package com.example.learning_management.course.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CourseSummary {
    private UUID id;
    private String subjectName;
    private String subjectCode;
    private String instructorName;
    private Integer maxStudents;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
}
