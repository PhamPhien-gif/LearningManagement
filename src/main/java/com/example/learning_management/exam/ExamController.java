package com.example.learning_management.exam;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {
    private final ExamService examService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{courseId}/addExam")
    public ResponseEntity<ExamDetail> createExam(
        @PathVariable UUID courseId,
        @RequestBody CreateExamRequest request,
        @AuthenticationPrincipal User instructor
    ){
        return ResponseEntity.ok(examService.addExam(courseId, request, instructor));
    }
}
