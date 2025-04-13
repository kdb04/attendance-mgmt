package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.repository.CourseRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) ->
        new Student(
            rs.getString("srn"),
            rs.getString("name"),
            rs.getInt("year_of_study")
        );

    public CourseService(CourseRepository courseRepository, JdbcTemplate jdbcTemplate) {
        this.courseRepository = courseRepository;
        this.jdbcTemplate = jdbcTemplate;
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

    public List<Student> getEnrolledStudents(String courseCode) {
        String sql = """
            SELECT 
                s.srn,
                s.name,
                s.year_of_study
            FROM 
                Students s
            JOIN 
                StudentEnrollments se ON s.srn = se.srn
            WHERE 
                se.course_code = ?
            ORDER BY 
                s.name
        """;
        
        return jdbcTemplate.query(sql, studentRowMapper, courseCode);
    }
}
