package com.example.learning_management.enrollment;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class EnrollmentSpecification {
    static public Specification<Enrollment> hasStudent(UUID studentId) {
        return (root, query, cb) -> {
            if (studentId == null) {
                return null;
            }
            return cb.equal(root.get("student").get("id"), studentId);
        };
    }

    static public Specification<Enrollment> hasPeriod(UUID periodId) {
        return (root, query, cb) -> {
            if (periodId == null) {
                return null;
            }
            return cb.equal(root.get("period").get("id"), periodId);
        };
    }
}
