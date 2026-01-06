package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.util.DBConnection;
import com.AttendanceManagementSystem.mapper.StudentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentMapper studentMapper;

    public StudentRepository(StudentMapper studentMapper) {
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
        this.studentMapper = studentMapper;
    }

    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM Students";
        return jdbcTemplate.query(sql, studentMapper);
    }

    public Optional<Student> getStudentById(String srn) {
        String sql = "SELECT * FROM Students WHERE srn = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studentMapper, srn));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int addStudent(Student student) {
        String sql = "INSERT INTO Students (srn, name, year_of_study) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, student.getSrn(), student.getName(), student.getYearOfStudy());
    }

    public int updateStudent(Student student) {
        String sql = "UPDATE Students SET name = ?, year_of_study = ? WHERE srn = ?";
        return jdbcTemplate.update(sql, student.getName(), student.getYearOfStudy(), student.getSrn());
    }

    public int deleteStudent(String srn) {
        String sql = "DELETE FROM Students WHERE srn = ?";
        return jdbcTemplate.update(sql, srn);
    }

    public String getPasswordHash(String srn){
        String sql = "SELECT password FROM Students WHERE srn = ?";
        return jdbcTemplate.queryForObject(sql, String.class, srn);
    }

    public void updatePasword(String srn, String passwordHash){
        String sql = "UPDATE Students SET password = ? WHERE srn = ?";
        jdbcTemplate.update(sql, passwordHash, srn);
    }
}
