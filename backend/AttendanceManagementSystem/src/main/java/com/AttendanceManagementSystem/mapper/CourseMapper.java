package com.AttendanceManagementSystem.mapper;

import com.AttendanceManagementSystem.model.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Course(
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("credits"),
            rs.getInt("total_classes")
        );
    }
}