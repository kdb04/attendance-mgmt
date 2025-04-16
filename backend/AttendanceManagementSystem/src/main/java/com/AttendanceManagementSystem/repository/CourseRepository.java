package com.AttendanceManagementSystem.repository;

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

    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) ->
        new Course(
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("credits")
        );

    //SELECT
    public List<Course> getAllCourses() {
        String sql = "SELECT * FROM Courses";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    //Conditional SELECT
    public Optional<Course> getCourseByCode(String courseCode) {
        String sql = "SELECT * FROM Courses WHERE course_code = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, courseRowMapper, courseCode));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return an empty Optional instead of throwing an error
        }
    }

    //INSERT
    public int addCourse(Course course) {
        // Calculate total classes based on credits
        int totalClasses;
        switch (course.getCredits()) {
            case 2: totalClasses = 20; break;
            case 3: totalClasses = 40; break;
            case 4: totalClasses = 60; break;
            case 5: totalClasses = 80; break;
            case 6: totalClasses = 100; break;
            default: totalClasses = 40; // default fallback
        }
        
        String sql = "INSERT INTO Courses (course_code, course_name, credits, total_classes) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, course.getCourseCode(), course.getCourseName(), course.getCredits(), totalClasses);
    }

    //UPDATE
    public int updateCourse(Course course) {
        String sql = "UPDATE Courses SET course_name = ?, credits = ? WHERE course_code = ?";
        return jdbcTemplate.update(sql, course.getCourseName(), course.getCredits(), course.getCourseCode());
    }

    //DELETE
    public int deleteCourse(String courseCode) {
        String sql = "DELETE FROM Courses WHERE course_code = ?";
        return jdbcTemplate.update(sql, courseCode);
    }
}
