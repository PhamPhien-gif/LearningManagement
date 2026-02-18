package com.example.learning_management.course;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private UUID id;
    private String subjectName;
    private String subjectCode;
    private String instructorName;
    private Integer maxStudents;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
}
