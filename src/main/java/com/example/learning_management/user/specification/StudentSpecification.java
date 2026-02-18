package com.example.learning_management.user.specification;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.example.learning_management.user.User;

public class StudentSpecification {
    static public Specification<User> hasEnrolledCourse(UUID courseId){
        return (root, query, cb) -> {
            if(courseId == null){
                return null;
            }
            return cb.equal(root.join("enrollments").get("course").get("id"), courseId);
        };
    }
}
