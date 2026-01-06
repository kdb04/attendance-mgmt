package com.AttendanceManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AttendanceManagementSystem.model.LoginRequest;
import com.AttendanceManagementSystem.model.LoginResponse;
import com.AttendanceManagementSystem.facade.LoginFacade;
import com.AttendanceManagementSystem.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final LoginFacade loginFacade;
    private final JwtUtil jwtUtil;

    public AuthController(LoginFacade loginFacade, JwtUtil jwtUtil) {
        this.loginFacade = loginFacade;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        
        String username = request.getUsername();
        LoginResponse response = null;

        //logger.info("AuthController.login() hit");
        //logger.info("Username = {}", request.getUsername());

        // Using facade to handle different types of login
        if ("admin".equals(username)) {
            response = loginFacade.adminLogin(username, request.getPassword());
        } else if (username.startsWith("PES")) {
            response = loginFacade.studentLogin(username, request.getPassword());
        } else if (username.startsWith("TRN")) {
            response = loginFacade.teacherLogin(username, request.getPassword());
        }

        if (response != null) {
            String token = jwtUtil.generateToken(response.getUserId(), response.getRole(), response.getName());

            //logger.info("JWT generated for user {}", username);

            return ResponseEntity.ok(
                new LoginResponse(
                    response.getUserId(),
                    response.getRole(),
                    response.getName(),
                    "Login successful",
                    token
                )
            );
        }

        logger.error("Login failed for username: {}", username);
        return ResponseEntity.badRequest().body(new LoginResponse(
            null, null, null, "Invalid credentials", null
        ));
    }
}