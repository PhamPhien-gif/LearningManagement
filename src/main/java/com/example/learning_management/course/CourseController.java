package com.example.learning_management.course;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_management.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    final private CourseService courseService;

    @PreAuthorize("hasRole('REGISTRAR')")
    @PostMapping("/create")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course, @AuthenticationPrincipal User registrar){
        return ResponseEntity.ok(courseService.createCourse(course, registrar));
    }
}
