package com.example.learning_management.enrollment;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.Course;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.enrollment.dto.DeleteEnrollmentResponse;
import com.example.learning_management.enrollment.dto.EnrollRequest;
import com.example.learning_management.enrollment.dto.EnrollResponse;
import com.example.learning_management.enrollment.dto.OpenPeriodRequest;
import com.example.learning_management.enrollment.dto.PeriodSummary;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final PeriodRepository periodRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public PeriodSummary openRegistrationPeriod(OpenPeriodRequest request) {
        // check valid time
        LocalDateTime timeBegin = request.getTimeBegin();
        LocalDateTime timeEnd = request.getTimeEnd();
        if (!timeBegin.isBefore(timeEnd)) {
            throw new AppException(ErrorCode.INVALID_PERIOD_TIME);
        }
        Period period = periodRepository.save(
                Period.builder()
                        .timeBegin(timeBegin)
                        .timeEnd(timeEnd)
                        .name(request.getName())
                        .build());
        return PeriodSummary.builder()
                .id(period.getId())
                .name(period.getName())
                .timeBegin(period.getTimeBegin())
                .timeEnd(period.getTimeEnd())
                .build();
    }

    @Transactional
    public EnrollResponse enrollCourse(EnrollRequest request, User student) {
        final UUID courseId = request.getCourseId();
        // find course
        Course course = courseRepository.findWithPeriodById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // check is enrollable
        checkEnrollable(course);

        // check is enrolled
        boolean isEronlled = enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId);
        if (isEronlled) {
            throw new AppException(ErrorCode.ALREADY_ENROLLED);
        }

        // optimisstic block (update and check course is ful)
        int successs = courseRepository.incrementCourseEnrollment(courseId);
        if (successs == 0) {
            throw new AppException(ErrorCode.COURSE_FULL);
        }

        // Has entityGraph, does not make another query
        Period period = course.getPeriod();
        Enrollment enrollment = enrollmentRepository.save(
                Enrollment.builder()
                        .course(course)
                        .student(student)
                        .build());

        LocalDateTime timeEndPeriod = period.getTimeEnd();
        LocalDateTime timeEndCourse = course.getTimeEnd();
        LocalDateTime timeEnroll = enrollment.getCreatedAt();
        // check time course and period
        if (timeEnroll.isAfter(timeEndPeriod) || timeEnroll.isAfter(timeEndCourse)) {
            throw new AppException(ErrorCode.REGISTRATION_CLOSED);
        }

        return EnrollResponse.builder()
                .courseId(courseId)
                .periodId(period.getId())
                .studentId(student.getId())
                .build();

    }

    private void checkEnrollable(Course course) {
        if (course == null) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (!course.getIsEnrollable()) {
            throw new AppException(ErrorCode.ENROLLMENT_DISABLED);
        }
        int currentStudents = course.getCurrentStudents();
        int maxStudents = course.getMaxStudents();
        if (currentStudents >= maxStudents) {
            throw new AppException(ErrorCode.COURSE_FULL);
        }
    }

    // check student enrollment, check role admin/registrar,
    @Transactional
    public DeleteEnrollmentResponse deleteEnrollment(UUID enrollmentId, User user) {
        final UUID userId = user.getId();
        final boolean isRegistrar = user.getRole() == Role.REGISTRAR;

        // if registrar: has permission to delete any enrollment
        int success;
        if (isRegistrar) {
            success = enrollmentRepository.deleteWithRoleRegistrarById(enrollmentId);
        } else {
            // else: can only delete his own enrollment
            success = enrollmentRepository.deleteByIdAndStudentId(enrollmentId, userId);
        }
        if (success == 0) {
            throw new AppException(ErrorCode.ENROLLMENT_DELETE_FAILED);
        }
        return DeleteEnrollmentResponse.builder()
                .build();
    }

}
