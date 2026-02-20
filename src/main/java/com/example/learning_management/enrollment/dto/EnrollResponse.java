package com.example.learning_management.enrollment.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollResponse {
    private UUID enrollmentId;
    private UUID studentId;
    private UUID courseId;
}
