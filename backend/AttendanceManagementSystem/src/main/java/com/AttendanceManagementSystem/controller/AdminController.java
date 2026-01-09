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

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
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

    @PostMapping("/teacher-assignments")
    public ResponseEntity<String> assignCourseToTeacher(@RequestBody Map<String, String> payload) {
        try {
            String trn = payload.get("trn");
            String courseCode = payload.get("courseCode");
            
            // Validate TRN and courseCode exist
            Teacher teacher = teacherService.getTeacherByTRN(trn);
            Course course = courseService.getCourseByCode(courseCode);
            
            if (teacher == null) {
                return ResponseEntity.badRequest().body("Teacher with TRN " + trn + " not found");
            }
            
            if (course == null) {
                return ResponseEntity.badRequest().body("Course with code " + courseCode + " not found");
            }
            
            boolean success = teacherService.assignCourseToTeacher(trn, courseCode);
            
            if (success) {
                return ResponseEntity.ok("Course assigned to teacher successfully");
            } else {
                return ResponseEntity.badRequest().body("Teacher is already assigned to this course");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error assigning course to teacher: " + e.getMessage());
        }
    }

    @PostMapping("/student-enrollments")
    public ResponseEntity<String> enrollStudentToCourse(@RequestBody Map<String, String> payload) {
        try {
            String srn = payload.get("srn");
            String courseCode = payload.get("courseCode");

            Student student = studentService.getStudentById(srn);
            Course course = courseService.getCourseByCode(courseCode);

            if (student == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found");
            }

            studentService.enrollStudentToCourse(srn, courseCode);
            return ResponseEntity.ok("Student enrolled successfully");

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Enrollment failed: " + e.getMessage());
        }
    }
}