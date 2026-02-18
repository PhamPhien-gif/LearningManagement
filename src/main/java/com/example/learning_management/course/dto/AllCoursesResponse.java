package com.example.learning_management.course.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllCoursesResponse {
    private List<CourseSummary> courses;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private boolean isLast;
    private int totalElements;
}
