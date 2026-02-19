package com.example.learning_management.enrollment;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.Course;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.enrollment.dto.EnrollRequest;
import com.example.learning_management.enrollment.dto.EnrollResponse;
import com.example.learning_management.enrollment.dto.OpenPeriodRequest;
import com.example.learning_management.enrollment.dto.PeriodSummary;
import com.example.learning_management.shared.AppException;
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

    // check time end period, time end course, check maxstudents, incre currenStudent, check enrollable
    @Transactional
    public EnrollResponse enrollCourse(EnrollRequest request, User student) {
        final UUID courseId = request.getCourseId();
        //find course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        //check is enrollable
        if(!course.getIsEnrollable()){
            throw new AppException(ErrorCode.ENROLLMENT_DISABLED);
        }

        //check is course full
        int maxStudents = course.getMaxStudents();
        int currentStudents = course.getCurrentStudents();
        if(currentStudents>= maxStudents){
            throw new AppException(ErrorCode.COURSE_FULL);
        }

        //optimisstic block (update and check course is ful)
        int successs = courseRepository.incrementCourseEnrollment(courseId);
        if(successs==0){
            throw new AppException(ErrorCode.COURSE_FULL);
        }

        Period period = course.getPeriod();
        Enrollment enrollment = enrollmentRepository.save(
                Enrollment.builder()
                        .course(course)
                        .student(student)
                        .build());

        LocalDateTime timeEndPeriod = period.getTimeEnd();
        LocalDateTime timeEndCourse = course.getTimeEnd();
        LocalDateTime timeEnroll = enrollment.getCreatedAt();
        //check time course and period
        if(timeEnroll.isAfter(timeEndPeriod) || timeEnroll.isAfter(timeEndCourse)){
            throw new AppException(ErrorCode.REGISTRATION_CLOSED);
        }
       
        return EnrollResponse.builder()
                            .courseId(courseId)
                            .periodId(period.getId())
                            .studentId(student.getId())
                            .build();
                            
    }

}
