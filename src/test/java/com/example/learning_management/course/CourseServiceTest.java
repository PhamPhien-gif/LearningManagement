package com.example.learning_management.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.dto.CourseSummary;
import com.example.learning_management.course.dto.CreateCourseRequest;
import com.example.learning_management.course.dto.CreateCourseResponse;
import com.example.learning_management.period.Period;
import com.example.learning_management.period.PeriodRepository;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import com.example.learning_management.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private PeriodRepository periodRepository;
    @Mock
    private UserRepository userRepository;

    private CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @InjectMocks
    private CourseService courseService;

    private final UUID instructorId = UUID.randomUUID();
    private final UUID periodId = UUID.randomUUID();
    private final UUID subjectId = UUID.randomUUID();
    private final UUID courseId = UUID.randomUUID();
    private final LocalDateTime timeBegin = LocalDateTime.now();
    private final LocalDateTime timeEnd = LocalDateTime.now().plusDays(23);

    @BeforeEach
    void setUp() {
        when(userRepository.existsByIdAndRole(any(), any())).thenReturn(true);
        lenient().when(subjectRepository.existsById(any())).thenReturn(true);
        lenient().when(periodRepository.existsById(any())).thenReturn(true);

    }

    private User createInstructor() {
        var instructor = User.builder()
                .email("instructor@gmail.com")
                .name("instructorName")
                .role(Role.INSTRUCTOR)
                .build();
        instructor.setId(instructorId);
        return instructor;
    }

    private CreateCourseRequest createRequest() {
        return CreateCourseRequest.builder()
                .instructorId(instructorId)
                .maxStudents(100)
                .periodId(periodId)
                .subjectId(subjectId)
                .timeBegin(timeBegin)
                .timeEnd(timeEnd)
                .build();
    }

    private Course createCourse() {
        Period period = periodRepository.getReferenceById(periodId);
        Course course = Course.builder()
                .maxStudents(100)
                .instructor(createInstructor())
                .subject(createSubject())
                .period(period)
                .timeBegin(timeBegin)
                .timeEnd(timeEnd)
                .build();
        course.setId(courseId);
        return course;
    }

    private Subject createSubject() {
        var subject = Subject.builder()
                .name("subjectName")
                .code("code")
                .build();
        subject.setId(subjectId);
        return subject;
    }

    private CourseSummary createResponse() {
        Course course = createCourse();
        return CourseSummary.from(course);
    }

    @Test
    void shouldReturnInstructorNotFound() {
        when(userRepository.existsByIdAndRole(any(), any())).thenReturn(false);
        AppException exception = assertThrows(AppException.class, () -> {
            courseService.createCourse(createRequest(), createInstructor());
        });

        assertEquals(ErrorCode.INSTRUCTOR_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shoudlReturnSubjectNotFound() {
        when(subjectRepository.existsById(any())).thenReturn(false);
        AppException exception = assertThrows(AppException.class, () -> {
            courseService.createCourse(createRequest(), createInstructor());
        });
        assertEquals(ErrorCode.SUBJECT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shoudlReturnPeroidNotFound() {
        when(periodRepository.existsById(any())).thenReturn(false);
        AppException exception = assertThrows(AppException.class, () -> {
            courseService.createCourse(createRequest(), createInstructor());
        });
        assertEquals(ErrorCode.PERIOD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldReturnInvalidCourseTime() {
        AppException exception = assertThrows(AppException.class, () -> {
            courseService.createCourse(createRequest(), createInstructor());
        });
        assertEquals(ErrorCode.INVALID_COURSE_TIME, exception.getErrorCode());
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        var response = createResponse();

        var savedCourse = createCourse();
        System.out.println("here: ");
        System.out.println(savedCourse);

        when(courseRepository.save(any())).thenReturn(savedCourse);
        var result = courseService.createCourse(createRequest(), createInstructor());
        assertEquals(result, response);
    }
}
