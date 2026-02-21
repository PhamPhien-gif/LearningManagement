package com.example.learning_management.course.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.learning_management.course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CourseSummary {
    private UUID id;
    private String subjectName;
    private String subjectCode;
    private String instructorName;
    private Integer maxStudents;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;

    public static CourseSummary from(Course course){
        return CourseSummary.builder()
                        .id(course.getId())
                        .subjectName(course.getSubject().getName())
                        .instructorName(course.getInstructor().getName())
                        .maxStudents(course.getMaxStudents())
                        .timeBegin(course.getTimeBegin())
                        .timeEnd(course.getTimeEnd())
                        .build();
    }
}
