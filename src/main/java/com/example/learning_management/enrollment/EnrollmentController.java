package com.example.learning_management.enrollment;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_management.enrollment.dto.DeleteEnrollmentResponse;
import com.example.learning_management.enrollment.dto.EnrollRequest;
import com.example.learning_management.enrollment.dto.EnrollResponse;
import com.example.learning_management.enrollment.dto.OpenPeriodRequest;
import com.example.learning_management.enrollment.dto.PeriodSummary;
import com.example.learning_management.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasRole('REGISTRAR')")
    @PostMapping("/openPeriod")
    public ResponseEntity<PeriodSummary> openRegistrationPeriod(@Valid @RequestBody OpenPeriodRequest request) {
        return ResponseEntity.ok(enrollmentService.openRegistrationPeriod(request));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enrollCourse")
    public ResponseEntity<EnrollResponse> enrollCourse(
            @Valid @RequestBody EnrollRequest request,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.ok(enrollmentService.enrollCourse(request, student));
    }

    @PreAuthorize("hasAnyRole('STUDENT','REGISTRAR')")
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<DeleteEnrollmentResponse> deleteEnrollment(
            @PathVariable UUID enrollmentId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(enrollmentService.deleteEnrollment(enrollmentId, user));
    }
}
