package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.util.DBConnection;
import com.AttendanceManagementSystem.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    // Use singleton database connection
    public CourseRepository() {
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
    }

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) ->
        new Course(
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("credits"),
            rs.getInt("total_classes")
        );

    // SELECT all courses
    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM Courses";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    // SELECT course by code
    public Optional<Course> getCourseByCode(String courseCode) {
        String sql = "SELECT * FROM Courses WHERE course_code = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, courseRowMapper, courseCode));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // INSERT new course
    public int addCourse(Course course) {
        String sql = "INSERT INTO Courses (course_code, course_name, credits, total_classes) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            course.getCourseCode(),
            course.getCourseName(), 
            course.getCredits(),
            course.getTotalClasses()
        );
    }

    // UPDATE existing course
    public int updateCourse(Course course) {
        String sql = "UPDATE Courses SET course_name = ?, credits = ?, total_classes = ? WHERE course_code = ?";
        return jdbcTemplate.update(sql,
            course.getCourseName(),
            course.getCredits(),
            course.getTotalClasses(),
            course.getCourseCode()
        );
    }

    // DELETE course
    public int deleteCourse(String courseCode) {
        String sql = "DELETE FROM Courses WHERE course_code = ?";
        return jdbcTemplate.update(sql, courseCode);
    }
}
