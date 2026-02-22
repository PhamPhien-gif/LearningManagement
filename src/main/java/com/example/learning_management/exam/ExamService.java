package com.example.learning_management.exam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.Course;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.enrollment.EnrollmentRepository;
import com.example.learning_management.exam.dto.AllExamsResponse;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;
import com.example.learning_management.exam.dto.ExamSummary;
import com.example.learning_management.exam.dto.UpdateExamRequest;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.shared.PageResponse;
import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public ExamDetail addExam(UUID courseId, CreateExamRequest request, User instructor) {
        final UUID instructorId = instructor.getId();

        boolean existCourse = courseRepository.existsByIdAndInstructorId(courseId, instructorId);
        if (!existCourse) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        Exam exam = examRepository.save(examMapper.fromRequestToExam(request));
        Course course = courseRepository.getReferenceById(courseId);
        exam.setCourse(course);
        return examMapper.toDetail(exam);
    }

    @Transactional
    public ExamDetail updateExam(UUID courseId, UUID examId, UpdateExamRequest request, User instructor) {
        final UUID instructorId = instructor.getId();
        // find exam
        Exam exam = examRepository.findWithCourseByIdAndCourseId(examId, courseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        // check instructor
        final UUID instructorOfCourse = exam.getCourse().getInstructor().getId();
        if (!instructorOfCourse.equals(instructorId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        examMapper.updateExamFromUpdateRequest(request, exam);
        examRepository.save(exam);
        return examMapper.toDetail(exam);
    }

    public ExamDetail viewExam(UUID courseId, UUID examId, User viewer) {
        // check permission
        if (!isHasPermission(courseId, viewer)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        // find exam
        Exam exam = examRepository.findWithCourseByIdAndCourseId(examId, courseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        // check time
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(exam.getTimeEnd())) {
            throw new AppException(ErrorCode.EXAM_EXPIRED);
        }

        return examMapper.toDetail(exam);
    }

    public AllExamsResponse getAllExams(UUID courseId, User viewer,Pageable pageable) {
        //check permission
        if (!isHasPermission(courseId, viewer)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        Page<Exam> examPage = examRepository.findAllByCourseId(courseId, pageable);
        PageResponse pageDetail = new PageResponse(examPage);

        List<ExamSummary> exams = new ArrayList<>();
        examPage.forEach(exam -> exams.add(examMapper.toSummary(exam)));
        
        return AllExamsResponse.builder()
                .exams(exams)
                .pageDetail(pageDetail)
                .build();

    }

    private boolean isHasPermission(UUID courseId, User viewer) {
        if (viewer == null) {
            return false;
        }

        boolean hasPermission = false;
        UUID viewerId = viewer.getId();
        if (viewer.getRole().equals(Role.INSTRUCTOR)) {
            hasPermission = courseRepository.existsByIdAndInstructorId(courseId, viewerId);
        } else {
            hasPermission = enrollmentRepository.existsByStudentIdAndCourseId(viewerId, courseId);
        }

        return hasPermission;
    }
}
