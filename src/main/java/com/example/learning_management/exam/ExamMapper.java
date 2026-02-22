package com.example.learning_management.exam;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.UpdateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    // @Mapping(source = "course.id", target = "courseId")
    ExamDetail toDetail(Exam exam);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "course", ignore = true)
    Exam fromRequestToExam(CreateExamRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateExamFromUpdateRequest(UpdateExamRequest request,@MappingTarget Exam exam);
}
