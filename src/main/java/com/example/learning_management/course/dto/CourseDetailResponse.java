package com.example.learning_management.course.dto;

import java.util.List;
import com.example.learning_management.material.dto.MaterialSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailResponse extends CourseSummary {
    private List<MaterialSummary> materials;

}
