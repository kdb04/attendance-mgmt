package com.AttendanceManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AttendanceManagementSystem.model.LoginRequest;
import com.AttendanceManagementSystem.model.LoginResponse;
import com.AttendanceManagementSystem.facade.LoginFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final LoginFacade loginFacade;

    public AuthController(LoginFacade loginFacade) {
        this.loginFacade = loginFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        
        String username = request.getUsername();
        LoginResponse response = null;

        // Using facade to handle different types of login
        if ("admin".equals(username)) {
            response = loginFacade.adminLogin(username, request.getPassword());
        } 
        else if (username.startsWith("PES")) {
            response = loginFacade.studentLogin(username);
        } 
        else if (username.startsWith("TRN")) {
            response = loginFacade.teacherLogin(username);
        }

        if (response != null) {
            logger.info("Login successful for user: {}", username);
            return ResponseEntity.ok(response);
        }

        logger.error("Login failed for username: {}", username);
        return ResponseEntity.badRequest().body(new LoginResponse(
            null, null, null, "Invalid credentials"
        ));
    }
}