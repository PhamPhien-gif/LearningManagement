package com.example.learning_management.exam;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.learning_management.exam.dto.CreateExamRequest;
import com.example.learning_management.exam.dto.ExamDetail;
import com.example.learning_management.exam.dto.UpdateExamRequest;
import com.example.learning_management.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/course/{courseId}/exams")
public class ExamController {
    private final ExamService examService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<ExamDetail> createExam(
            @PathVariable UUID courseId,
            @RequestBody CreateExamRequest request,
            @AuthenticationPrincipal User instructor) {
        return ResponseEntity.ok(examService.addExam(courseId, request, instructor));
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("/{examId}")
    public ResponseEntity<ExamDetail> updateExam(
        @PathVariable UUID courseId,
        @PathVariable UUID examId,
        @RequestBody UpdateExamRequest request,
        @AuthenticationPrincipal User instructor
    ){
        return ResponseEntity.ok(examService.updateExam(courseId, examId, request, instructor));
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR','STUDENT')")
    @GetMapping("/{examId}")
    public ResponseEntity<ExamDetail> viewExxam(
        @PathVariable UUID courseId,
        @PathVariable UUID examId,
        @AuthenticationPrincipal User viewer
    ){
        return ResponseEntity.ok(examService.viewExam(courseId, examId, viewer));
    }
}
