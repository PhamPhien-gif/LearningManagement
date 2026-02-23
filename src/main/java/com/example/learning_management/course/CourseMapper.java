package com.example.learning_management.course;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.learning_management.course.dto.CreateCourseRequest;

@Mapper(componentModel = "Spring")
public interface CourseMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course getCourseFromRequest(CreateCourseRequest request);
}
