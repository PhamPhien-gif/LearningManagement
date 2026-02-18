package com.example.learning_management.course;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.dto.AllCoursesResponse;
import com.example.learning_management.course.dto.CourseSummary;
import com.example.learning_management.course.dto.CreateCourseRequest;
import com.example.learning_management.course.dto.CreateCourseResponse;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import com.example.learning_management.user.UserRepository;
import lombok.RequiredArgsConstructor;

import static com.example.learning_management.course.CourseSpecification.hasInstructor;
import static com.example.learning_management.course.CourseSpecification.hasPeriod;
import static com.example.learning_management.course.CourseSpecification.hasStudent;
import static com.example.learning_management.course.CourseSpecification.hasSubject;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class) // rollback when catch exception, default readOnly
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public CreateCourseResponse createCourse(CreateCourseRequest request, User registrar) {

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
        if (!timeBegin.isBefore(timeEnd)) {
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

        return CreateCourseResponse.builder()
                .id(successCourse.getId())
                .instructorName(instructor.getName())
                .subjectName(subject.getName())
                .subjectCode(subject.getCode())
                .maxStudents(maxStudents)
                .timeBegin(timeBegin)
                .timeEnd(timeEnd)
                .build();
    }

    // public GetDetailCoursesResponse getDetailCourse(UUID courseId, User viewer){

    // Boolean isEnrolled =
    // }

    // public Boolean isEnrolled(){

    // }

    // N+1 Query
    public AllCoursesResponse getAllCourses(UUID periodId, UUID studentId, UUID instructorId, UUID subjectId,
            Pageable pageable) {
        //get all courses, using jpa specification        
        Page<Course> courses = courseRepository.findAll(where(hasPeriod(periodId)
                .and(hasStudent(studentId))
                .and(hasInstructor(instructorId))
                .and(hasSubject(subjectId))), pageable);

        //build list course summaries
        List<CourseSummary> courseSummaries = new ArrayList<>();

        courses.forEach(course -> courseSummaries.add(
                CourseSummary.builder()
                        .id(course.getId())
                        .instructorName(course.getInstructor().getName())
                        .subjectName(course.getSubject().getName())
                        .subjectCode(course.getSubject().getCode())
                        .maxStudents(course.getMaxStudents())
                        .timeBegin(course.getTimeBegin())
                        .timeEnd(course.getTimeEnd())
                        .build()));

        return AllCoursesResponse.builder()
                .courses(courseSummaries)
                .pageNumber(courses.getNumber())
                .pageSize(courses.getSize())
                .totalPages(courses.getTotalPages())
                .totalElements(courses.getNumberOfElements())
                .isLast(courses.isLast())
                .build();
    }

}
