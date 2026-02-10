package com.example.learning_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearningManagementApplication {

	public static void main(String[] args) {
        // System.out.println(${DATABASE_URL});
		System.out.println(System.getenv("DB_URL"));
		SpringApplication.run(LearningManagementApplication.class, args);
	}

}
