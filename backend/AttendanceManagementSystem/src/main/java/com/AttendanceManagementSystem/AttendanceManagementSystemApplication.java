package com.AttendanceManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class AttendanceManagementSystemApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceManagementSystemApplication.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(AttendanceManagementSystemApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Spring Boot is working!";
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Displaying Student table data:");
        
        try {
            List<Map<String, Object>> students = jdbcTemplate.queryForList("SELECT * FROM Students");
            
            if (students.isEmpty()) {
                logger.info("No records found in Students table");
            } else {
                for (Map<String, Object> student : students) {
                    logger.info("Student: {}", student);
                }
                logger.info("Total students found: {}", students.size());
            }
        } catch (Exception e) {
            logger.error("Error querying Student table: {}", e.getMessage(), e);
        }
    }
}