package com.example.learning_management.enrollment.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EnrollRequest {
    @NotNull(message = "Course must not be null")
    private UUID courseId;
}
