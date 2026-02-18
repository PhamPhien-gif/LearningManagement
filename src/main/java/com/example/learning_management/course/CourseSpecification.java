package com.example.learning_management.course;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    public static Specification<Course> hasPeriod(UUID periodId) {
        return (root, query, cb) -> {
            if (periodId == null) {
                return null;
            }
            return cb.equal(root.get("period_id"), periodId);
        };
    }

    public static Specification<Course> hasInstructor(UUID instructorId) {
        return (root, query, cb) -> {
            if (instructorId == null) {
                return null;
            }
            return cb.equal(root.get("instructor").get("id"), instructorId);
        };
    }

    public static Specification<Course> hasStudent(UUID studentId) {
        return (root, query, cb) -> {
            if (studentId == null) {
                return null;
            }
            return cb.equal(root.get("students").get("id"), studentId);
        };
    }

    public static Specification<Course> hasSubject(UUID subjectId) {
        return (root, query, cb) -> {
            if (subjectId == null) {
                return null;
            }
            return cb.equal(root.get("subject").get("id"), subjectId);
        };
    }

}
