package com.example.learning_management.exam;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    // @Mapping(source = "course.id", target = "courseId")
    ExamDetail toDetail(Exam exam);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "course", ignore = true)
    Exam fromRequestToExam(CreateExamRequest request);
}
