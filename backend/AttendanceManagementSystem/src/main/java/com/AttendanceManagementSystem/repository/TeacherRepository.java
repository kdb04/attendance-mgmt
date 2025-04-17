package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.util.DBConnection;
import com.AttendanceManagementSystem.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherRepository {

    private final JdbcTemplate jdbcTemplate;

    public TeacherRepository() {
        // Get JdbcTemplate from singleton instance
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
    }

    private final RowMapper<Teacher> teacherRowMapper = (rs, rowNum) ->
        new Teacher(
            rs.getString("trn"),
            rs.getString("name")
        );

    //SELECT
    public List<Teacher> getAllTeachers() {
        String sql = "SELECT * FROM Teachers";
        return jdbcTemplate.query(sql, teacherRowMapper);
    }

    //Conditional SELECT
    public Optional<Teacher> getTeacherByTRN(String trn) {
        String sql = "SELECT * FROM Teachers WHERE trn = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, teacherRowMapper, trn));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return an empty Optional instead of throwing an error
        }
    }

    //INSERT
    public int addTeacher(Teacher teacher) {
        String sql = "INSERT INTO Teachers (trn, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, teacher.getTRN(), teacher.getName());
    }

    //UPDATE
    public int updateTeacher(Teacher teacher) {
        String sql = "UPDATE Teachers SET name = ? WHERE trn = ?";
        return jdbcTemplate.update(sql, teacher.getName(), teacher.getTRN());
    }

    //DELETE
    public int deleteTeacher(String trn) {
        String sql = "DELETE FROM Teachers WHERE trn = ?";
        return jdbcTemplate.update(sql, trn);
    }
}
