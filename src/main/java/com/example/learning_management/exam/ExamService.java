package com.example.learning_management.exam;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.Course;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;
import com.example.learning_management.exam.dto.UpdateExamRequest;
import com.example.learning_management.shared.AppException;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;

    @Transactional
    public ExamDetail addExam(UUID courseId, CreateExamRequest request, User instructor){
        final UUID instructorId = instructor.getId(); 
        
        boolean existCourse = courseRepository.existsByIdAndInstructorId(courseId, instructorId);
        if(!existCourse){
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        
        Exam exam = examRepository.save(examMapper.fromRequestToExam(request));
        Course course = courseRepository.getReferenceById(courseId);
        exam.setCourse(course);
        return examMapper.toDetail(exam);
    }

    @Transactional
    public ExamDetail updateExam(UUID courseId, UUID examId, UpdateExamRequest request, User instructor){
        final UUID instructorId = instructor.getId();
        //find exam
        Exam exam = examRepository.findWithCourseByIdAndCourseId(examId, courseId)
            .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        //check instructor
        final UUID instructorOfCourse = exam.getCourse().getInstructor().getId();
        if(!instructorOfCourse.equals(instructorId)){
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        examMapper.updateExamFromUpdateRequest(request, exam);
        examRepository.save(exam);
        return examMapper.toDetail(exam);
    }
}
