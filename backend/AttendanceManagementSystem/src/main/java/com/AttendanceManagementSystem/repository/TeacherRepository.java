package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.util.DBConnection;
import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.mapper.TeacherMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherMapper teacherMapper;

    public TeacherRepository(TeacherMapper teacherMapper) {
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
        this.teacherMapper = teacherMapper;
    }

    public List<Teacher> getAllTeachers() {
        String sql = "SELECT * FROM Teachers";
        return jdbcTemplate.query(sql, teacherMapper);
    }

    public Optional<Teacher> getTeacherByTRN(String trn) {
        String sql = "SELECT * FROM Teachers WHERE trn = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, teacherMapper, trn));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int addTeacher(Teacher teacher) {
        String sql = "INSERT INTO Teachers (trn, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, teacher.getTRN(), teacher.getName());
    }

    public int updateTeacher(Teacher teacher) {
        String sql = "UPDATE Teachers SET name = ? WHERE trn = ?";
        return jdbcTemplate.update(sql, teacher.getName(), teacher.getTRN());
    }

    public int deleteTeacher(String trn) {
        String sql = "DELETE FROM Teachers WHERE trn = ?";
        return jdbcTemplate.update(sql, trn);
    }

    public String getPasswordHash(String trn){
        String sql = "SELECT password FROM Teachers WHERE trn = ?";
        return jdbcTemplate.queryForObject(sql, String.class, trn);
    }

    public void updatePasword(String trn, String passwordHash){
        String sql = "UPDATE Teachers SET password = ? WHERE trn = ?";
        jdbcTemplate.update(sql, passwordHash, trn);
    }
}
