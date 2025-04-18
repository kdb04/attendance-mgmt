package com.AttendanceManagementSystem.mapper;

import com.AttendanceManagementSystem.model.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper implements RowMapper<Teacher> {
    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Teacher(
            rs.getString("trn"),
            rs.getString("name")
        );
    }
}