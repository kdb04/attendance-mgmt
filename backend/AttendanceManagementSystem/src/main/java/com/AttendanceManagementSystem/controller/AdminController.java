package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.model.User;
import com.AttendanceManagementSystem.service.StudentService;
import com.AttendanceManagementSystem.service.TeacherService;
import com.AttendanceManagementSystem.service.CourseService;
import com.AttendanceManagementSystem.factory.UserFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {

    private final UserFactory userFactory;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public AdminController(UserFactory userFactory, StudentService studentService, TeacherService teacherService, CourseService courseService) {
        this.userFactory = userFactory;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student studentRequest) {
        try {
            User student = userFactory.createUser("STUDENT", 
                studentRequest.getSrn(),
                studentRequest.getName(),
                String.valueOf(studentRequest.getYearOfStudy())
            );
            studentService.addStudent((Student) student);
            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding student: " + e.getMessage());
        }
    }

    @PostMapping("/teachers")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacherRequest) {
        try {
            User teacher = userFactory.createUser("TEACHER",
                teacherRequest.getTRN(),
                teacherRequest.getName()
            );
            teacherService.addTeacher((Teacher) teacher);
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
            
            // Validate credits are within the acceptable range
            if (course.getCredits() < 2 || course.getCredits() > 6) {
                return ResponseEntity.badRequest().body("Credits must be between 2 and 6");
            }
            
            // The total classes are automatically set in the Course model and repository
            courseService.addCourse(course);
            return ResponseEntity.ok("Course added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding course: " + e.getMessage());
        }
    }
}