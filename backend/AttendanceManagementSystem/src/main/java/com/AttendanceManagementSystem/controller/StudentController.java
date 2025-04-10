package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{srn}")
    public ResponseEntity<Student> getStudentBySrn(@PathVariable String srn) {
        try {
            Student student = studentService.getStudentById(srn);
            if (student != null) {
                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{srn}/courses")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable String srn) {
        try {
            List<Course> enrolledCourses = studentService.getEnrolledCourses(srn);
            return ResponseEntity.ok(enrolledCourses);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        try {
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create student: " + e.getMessage());
        }
    }

    @PutMapping("/{srn}")
    public ResponseEntity<String> updateStudent(@PathVariable String srn, @RequestBody Student student) {
        if (!srn.equals(student.getSrn())) {
            return ResponseEntity.badRequest().body("SRN in URL doesn't match request body");
        }
        try {
            int rowsAffected = studentService.updateStudent(student);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Student updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating student: " + e.getMessage());
        }
    }

    @DeleteMapping("/{srn}")
    public ResponseEntity<String> deleteStudent(@PathVariable String srn) {
        try {
            int rowsAffected = studentService.deleteStudent(srn);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Student deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting student: " + e.getMessage());
        }
    }
}
