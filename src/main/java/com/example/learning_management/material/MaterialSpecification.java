package com.example.learning_management.material;

import org.springframework.data.jpa.domain.Specification;

public class MaterialSpecification {

    // has not use for any service
    static public Specification<Material> hasEnrolled(boolean isEnrolled) {
        // if Enrolled -> get all material
        return (root, query, cb) -> {
            if (!isEnrolled) {
                return null;
            }
            // not enrolled -> get previewd material
            return cb.equal(root.get("isPreview"), true);
        };
    }
}
