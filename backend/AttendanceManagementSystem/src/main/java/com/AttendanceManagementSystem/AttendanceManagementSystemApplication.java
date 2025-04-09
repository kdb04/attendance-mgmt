package com.AttendanceManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan(basePackages="com.AttendanceManagementSystem")
@RestController
public class AttendanceManagementSystemApplication{

    private static final Logger logger = LoggerFactory.getLogger(AttendanceManagementSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AttendanceManagementSystemApplication.class, args);
        logger.info("Application started successfully");
    }

    @GetMapping("/")
    public String hello() {
        return "Spring Boot is working!";
    }
}
