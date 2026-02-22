package com.example.learning_management.exam.dto;

import java.util.List;

import com.example.learning_management.shared.PageResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AllExamsResponse {
    private PageResponse pageDetail;
    private List<ExamSummary> exams;
}
