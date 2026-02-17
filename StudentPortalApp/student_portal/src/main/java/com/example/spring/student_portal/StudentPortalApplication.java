package com.example.spring.student_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StudentPortalApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(StudentPortalApplication.class, args);
	}

}
