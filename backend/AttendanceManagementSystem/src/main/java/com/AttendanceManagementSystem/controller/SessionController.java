package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.ClassSession;
import com.AttendanceManagementSystem.model.AttendanceRecord;
import com.AttendanceManagementSystem.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody ClassSession session) {
        try {
            Integer sessionId = sessionService.createSession(session);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "sessionId", sessionId,
                    "message", "Session created successfully"
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create session: " + e.getMessage()));
        }
    }

    @PutMapping("/attendance")
    public ResponseEntity<Map<String, String>> updateAttendance(@RequestBody AttendanceRecord record) {
        try {
            sessionService.updateAttendance(record);
            return ResponseEntity.ok(Map.of("message", "Attendance updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update attendance: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseCode}/teacher/{trn}")
    public ResponseEntity<List<ClassSession>> getSessionsByCourseAndTeacher(
            @PathVariable String courseCode, 
            @PathVariable String trn) {
        try {
            List<ClassSession> sessions = sessionService.getSessionsByCourseAndTeacher(courseCode, trn);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{sessionId}/attendance")
    public ResponseEntity<List<Map<String, Object>>> getAttendanceBySession(@PathVariable Integer sessionId) {
        try {
            List<Map<String, Object>> attendance = sessionService.getAttendanceBySession(sessionId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}