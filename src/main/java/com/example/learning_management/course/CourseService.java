package com.example.learning_management.course;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import com.example.learning_management.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class) // rollback when catch exception, default readOnly
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Transactional   
    public CourseResponse createCourse(CreateCourseRequest request, User registrar) {

        // check instructor
        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // check subject
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        final Integer maxStudents = request.getMaxStudents();
        if (maxStudents == null || maxStudents <= 0 || maxStudents > 120) {
            throw new AppException(ErrorCode.INVALID_COURSE_CAPACITY);
        }

        final LocalDateTime timeBegin = request.getTimeBegin();
        final LocalDateTime timeEnd = request.getTimeEnd();
        if(!timeBegin.isBefore(timeEnd)){
            throw new AppException(ErrorCode.INVALID_COURSE_TIME);
        }

        Course newCourse = Course.builder()
                .registrar(registrar)
                .subject(subject)
                .instructor(instructor)
                .maxStudents(maxStudents)
                .timeBegin(timeBegin)
                .timeEnd(timeEnd)
                .build();
        Course successCourse = courseRepository.save(newCourse);

        return CourseResponse.builder()
                            .id(successCourse.getId())
                            .instructorName(instructor.getName())
                            .subjectName(subject.getName())
                            .subjectCode(subject.getCode())
                            .maxStudents(maxStudents)
                            .timeBegin(timeBegin)
                            .timeEnd(timeEnd)
                            .build();
    }

}
