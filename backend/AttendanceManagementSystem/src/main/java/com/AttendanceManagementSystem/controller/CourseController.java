package com.AttendanceManagementSystem.controller;

import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String courseCode) {
        try {
            Course course = courseService.getCourseByCode(courseCode);
            if (course != null) {
                return ResponseEntity.ok(course);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addCourse(@RequestBody Course course) {
        try {
            courseService.addCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create course: " + e.getMessage());
        }
    }

    @PutMapping("/{courseCode}")
    public ResponseEntity<String> updateCourse(@PathVariable String courseCode, @RequestBody Course course) {
        if (!courseCode.equals(course.getCourseCode())) {
            return ResponseEntity.badRequest().body("Course code in URL doesn't match request body");
        }
        try {
            int rowsAffected = courseService.updateCourse(course);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Course updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating course: " + e.getMessage());
        }
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity<String> deleteCourse(@PathVariable String courseCode) {
        try {
            int rowsAffected = courseService.deleteCourse(courseCode);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Course deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting course: " + e.getMessage());
        }
    }
}
