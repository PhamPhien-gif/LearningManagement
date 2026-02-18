package com.example.learning_management.course;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_management.course.dto.CreateCourseRequest;
import com.example.learning_management.course.dto.CreateCourseResponse;
import com.example.learning_management.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;
    private final Integer sizePage = 10;
    private final String sortBy = "timeBegin";

    @PreAuthorize("hasRole('REGISTRAR')")
    @PostMapping("/create")
    public ResponseEntity<CreateCourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal User registrar) {
        return ResponseEntity.ok(courseService.createCourse(request, registrar));
    }

    @GetMapping("/getAll")
    public Page<Course> getAllCourse(
            @RequestParam(required = false) UUID periodId,
            @RequestParam(required = false) UUID instructorId,
            @RequestParam(required = false) UUID subjectId,
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false, defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, sizePage, Sort.by(sortBy).descending());
        return courseService.getAllCourses(periodId, studentId, instructorId, subjectId, pageable);
    }
}
