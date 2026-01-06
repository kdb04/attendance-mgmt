package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.service.TeacherService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "${FRONTEND_ORIGIN}", allowCredentials = "true")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{trn}")
    public ResponseEntity<Teacher> getTeacherByTRN(@PathVariable String trn) {
        try {
            Teacher teacher = teacherService.getTeacherByTRN(trn);
            if (teacher != null) {
                return ResponseEntity.ok(teacher);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        try {
            teacherService.addTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                "Teacher created successfully"
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Failed to create teacher: " + e.getMessage());
        }
    }

    @PutMapping("/{trn}")
    public ResponseEntity<String> updateTeacher(
        @PathVariable String trn,
        @RequestBody Teacher teacher
    ) {
        if (!trn.equals(teacher.getTRN())) {
            return ResponseEntity.badRequest()
                .body("TRN in URL doesn't match request body");
        }
        try {
            int rowsAffected = teacherService.updateTeacher(teacher);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Teacher updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Error updating teacher: " + e.getMessage()
            );
        }
    }

    @DeleteMapping("/{trn}")
    public ResponseEntity<String> deleteTeacher(@PathVariable String trn) {
        try {
            int rowsAffected = teacherService.deleteTeacher(trn);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Teacher deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Error deleting teacher: " + e.getMessage()
            );
        }
    }

    @GetMapping("/{trn}/courses")
    public ResponseEntity<List<Course>> getAssignedCourses(
        @PathVariable String trn
    ) {
        try {
            //System.out.println("Fetching courses for teacher: " + trn);
            List<Course> assignedCourses = teacherService.getAssignedCourses(trn);
            //System.out.println("Found courses: " + assignedCourses);
            return ResponseEntity.ok(assignedCourses);
        } catch (Exception e) {
            //System.err.println("Error fetching courses: " + e.getMessage());
            //e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
