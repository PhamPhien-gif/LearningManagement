package com.example.learning_management.course.dto;

import java.util.List;
import com.example.learning_management.shared.PageResponse;
import com.example.learning_management.user.dto.StudentSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AllCourseStudentReponse{
    private PageResponse pageDetail;
    private List<StudentSummary> students;
}
