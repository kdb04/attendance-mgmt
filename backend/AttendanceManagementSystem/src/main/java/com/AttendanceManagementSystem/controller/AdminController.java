package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.service.StudentService;
import com.AttendanceManagementSystem.service.TeacherService;
import com.AttendanceManagementSystem.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public AdminController(StudentService studentService, TeacherService teacherService, CourseService courseService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        try {
            // Validate SRN format
            if (!student.getSrn().matches("PES2UG\\d{2}CS\\d{3}")) {
                return ResponseEntity.badRequest().body("Invalid SRN format");
            }
            studentService.addStudent(student);
            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding student: " + e.getMessage());
        }
    }

    @PostMapping("/teachers")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        try {
            // Validate TRN format
            if (!teacher.getTRN().matches("TRN\\d{3}")) {
                return ResponseEntity.badRequest().body("Invalid TRN format");
            }
            teacherService.addTeacher(teacher);
            return ResponseEntity.ok("Teacher added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding teacher: " + e.getMessage());
        }
    }

    @PostMapping("/courses")
    public ResponseEntity<String> addCourse(@RequestBody Course course) {
        try {
            // Validate course code format
            if (!course.getCourseCode().matches("[A-Z]{2}\\d{3}")) {
                return ResponseEntity.badRequest().body("Invalid course code format");
            }
            courseService.addCourse(course);
            return ResponseEntity.ok("Course added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding course: " + e.getMessage());
        }
    }
}