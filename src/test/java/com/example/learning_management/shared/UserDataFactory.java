package com.example.learning_management.shared;

import java.util.UUID;

import org.springframework.security.core.userdetails.User.UserBuilder;

import com.example.learning_management.user.Role;
import com.example.learning_management.user.User;

public class UserDataFactory {
    final static UUID instructorId = UUID.randomUUID();
    public static User createInstructor(){
        User instructor = User.builder()
                            .email("instructorEamil@gmail.com")
                            .name("instructorName")
                            .role(Role.INSTRUCTOR)
                            .build();
        instructor.setId(instructorId);
        return instructor;
    }

    
}
