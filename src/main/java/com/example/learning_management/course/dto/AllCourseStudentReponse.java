package com.example.learning_management.course.dto;

import java.util.List;
import java.util.UUID;
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
public class AllCourseStudentReponse extends PageResponse{
    private List<StudentSummary> students;
    private UUID instructorId;
}
