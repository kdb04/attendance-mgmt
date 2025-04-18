package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.util.DBConnection;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.mapper.CourseMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CourseMapper courseMapper;

    public CourseRepository(CourseMapper courseMapper) {
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
        this.courseMapper = courseMapper;
    }

    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM Courses";
        return jdbcTemplate.query(sql, courseMapper);
    }

    public Optional<Course> getCourseByCode(String courseCode) {
        String sql = "SELECT * FROM Courses WHERE course_code = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, courseMapper, courseCode));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int addCourse(Course course) {
        String sql = "INSERT INTO Courses (course_code, course_name, credits, total_classes) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            course.getCourseCode(),
            course.getCourseName(), 
            course.getCredits(),
            course.getTotalClasses()
        );
    }

    public int updateCourse(Course course) {
        String sql = "UPDATE Courses SET course_name = ?, credits = ?, total_classes = ? WHERE course_code = ?";
        return jdbcTemplate.update(sql,
            course.getCourseName(),
            course.getCredits(),
            course.getTotalClasses(),
            course.getCourseCode()
        );
    }

    public int deleteCourse(String courseCode) {
        String sql = "DELETE FROM Courses WHERE course_code = ?";
        return jdbcTemplate.update(sql, courseCode);
    }
}
