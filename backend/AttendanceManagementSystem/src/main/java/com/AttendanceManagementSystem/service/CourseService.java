package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    public Course getCourseByCode(String courseCode) {
        Optional<Course> course = courseRepository.getCourseByCode(courseCode);
        return course.orElse(null);
    }

    public void addCourse(Course course) {
        courseRepository.addCourse(course);
    }

    public int updateCourse(Course course) {
        return courseRepository.updateCourse(course);
    }

    public int deleteCourse(String courseCode) {
        return courseRepository.deleteCourse(courseCode);
    }
}
