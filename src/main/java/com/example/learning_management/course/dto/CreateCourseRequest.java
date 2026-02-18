package com.example.learning_management.course.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCourseRequest {

    @NotNull(message = "Subject must not be null")
    private UUID subjectId;

    @NotNull(message = "Instructor must not be null")
    private UUID instructorId;

    @Min(value = 1, message = "INVALID_COURSE_CAPACITY")
    @Max(value = 120, message = "INVALID_COURSE_CAPACITY")
    private Integer maxStudents;

    @NotNull(message = "Time begin must not be null")
    private LocalDateTime timeBegin;
    @NotNull(message = "Time end must not be null")
    private LocalDateTime timeEnd;
}
