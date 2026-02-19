package com.example.learning_management.enrollment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_management.enrollment.dto.OpenPeriodRequest;
import com.example.learning_management.enrollment.dto.PeriodSummary;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasRole(REGISTRAR)")
    @PostMapping("/period")
    public ResponseEntity<PeriodSummary> openRegistrationPeriod(@Valid @RequestBody OpenPeriodRequest request){
        return ResponseEntity.ok(enrollmentService.openRegistrationPeriod(request));
    }
}
