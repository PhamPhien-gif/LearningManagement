package com.example.learning_management.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeleteEnrollmentResponse {
    @Builder.Default
    private String message = "Deleted Successfully";
}
