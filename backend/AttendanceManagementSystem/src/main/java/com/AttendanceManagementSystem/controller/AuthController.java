package com.AttendanceManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AttendanceManagementSystem.model.LoginRequest;
import com.AttendanceManagementSystem.model.LoginResponse;
import com.AttendanceManagementSystem.service.StudentService;
import com.AttendanceManagementSystem.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());

        String username = request.getUsername();
        
        // Check if it's admin
        if ("admin".equals(username) && "admin123#".equals(request.getPassword())) {
            return ResponseEntity.ok(new LoginResponse("admin", "ADMIN", "Admin user", "Login successful"));
        }

        // Check if it's a student (PES prefix)
        if (username.startsWith("PES")) {
            var student = studentService.getStudentById(username);
            if (student != null) {
                return ResponseEntity.ok(new LoginResponse(
                    username, 
                    "STUDENT", 
                    student.getName(),
                    "Login successful"
                ));
            }
        }

        // Check if it's a teacher (TRN prefix)
        if (username.startsWith("TRN")) {
            var teacher = teacherService.getTeacherByTRN(username);
            if (teacher != null) {
                return ResponseEntity.ok(new LoginResponse(
                    username,
                    "TEACHER",
                    teacher.getName(),
                    "Login successful"
                ));
            }
        }

        logger.error("Login failed for username: {}", username);
        return ResponseEntity.badRequest().body(new LoginResponse(
            null, null, null, "Invalid credentials"
        ));
    }
}