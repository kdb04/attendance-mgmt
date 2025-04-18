package com.AttendanceManagementSystem.mapper;

import com.AttendanceManagementSystem.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
            rs.getString("srn"),
            rs.getString("name"),
            rs.getInt("year_of_study")
        );
    }
}