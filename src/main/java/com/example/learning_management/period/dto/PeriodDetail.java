package com.example.learning_management.period.dto;

import java.util.List;
import com.example.learning_management.course.dto.CourseSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PeriodDetail {
    PeriodSummary periodSummary;
    List<CourseSummary> courses;

}
