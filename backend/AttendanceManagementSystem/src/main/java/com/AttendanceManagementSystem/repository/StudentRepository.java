package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository  //Spring Repository(DAO)
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository() {
        // Get JdbcTemplate from singleton instance
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
    }

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) ->
        new Student(
            rs.getString("srn"),
            rs.getString("name"),
            rs.getInt("year_of_study")
        );

    //SELECT query 
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM Students";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    //Conditional SELECT query 
    public Optional<Student> getStudentById(String srn) {
        String sql = "SELECT * FROM Students WHERE srn = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studentRowMapper, srn));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return an empty Optional instead of throwing an error
        }
    }

    //INSERT query
    public int addStudent(Student student) {
        String sql = "INSERT INTO Students (srn, name, year_of_study) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, student.getSrn(), student.getName(), student.getYearOfStudy());
    }

    //UPDATE query
    public int updateStudent(Student student) {
        String sql = "UPDATE Students SET name = ?, year_of_study = ? WHERE srn = ?";
        return jdbcTemplate.update(sql, student.getName(), student.getYearOfStudy(), student.getSrn());
    }

    //DELETE query
    public int deleteStudent(String srn) {
        String sql = "DELETE FROM Students WHERE srn = ?";
        return jdbcTemplate.update(sql, srn);
    } 
}
