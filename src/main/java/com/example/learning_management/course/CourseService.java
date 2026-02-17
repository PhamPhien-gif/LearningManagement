package com.example.learning_management.course;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
   
    public Course createCourse(Course course, User registrar) {

        //get the real instructor, subject after valid
        InnerCourseService data = validCourse(course);
        
        Course newCourse = Course.builder()
                .registrar(registrar)
                .subject(data.subject)
                .instructor(data.instructor)
                .maxStudents(course.getMaxStudents())
                .timeBegin(course.getTimeBegin())
                .timeEnd(course.getTimeEnd())
                .build();
        return courseRepository.save(newCourse);
    }

    private record InnerCourseService(User instructor, Subject subject) {
    }

    private InnerCourseService validCourse(Course course) {

        // check instructor
        User instructor = userRepository.findById(course.getInstructor().getId())
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        if (instructor.getRole() != Role.INSTRUCTOR) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // check subject
        Subject subject = subjectRepository.findById(course.getSubject().getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        final Integer maxStudent = course.getMaxStudents();
        if (maxStudent == null || maxStudent <= 0 || maxStudent > 120) {
            throw new AppException(ErrorCode.INVALID_COURSE_CAPACITY);
        }

        return new InnerCourseService(instructor, subject);
    }

}
