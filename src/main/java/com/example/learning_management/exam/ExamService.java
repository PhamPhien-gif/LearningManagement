package com.example.learning_management.exam;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning_management.config.ErrorCode;
import com.example.learning_management.course.CourseRepository;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;
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

    //roll instructor
    @Transactional
    public ExamDetail addExam(UUID courseId, CreateExamRequest request, User instructor){
        final UUID instructorId = instructor.getId(); 
        
        boolean existCourse = courseRepository.existsByIdAndInstructorId(courseId, instructorId);
        if(!existCourse){
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        
        Exam exam = examRepository.save(examMapper.fromRequestToExam(request));
        return examMapper.toDetail(exam);
    }
}
