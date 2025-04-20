package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.repository.TeacherRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final JdbcTemplate jdbcTemplate;

    // Add courseRowMapper definition
    private final RowMapper<Course> courseRowMapper = (rs, rowNum) ->
        new Course(
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("credits")
        );

    public TeacherService(TeacherRepository teacherRepository, JdbcTemplate jdbcTemplate) {
        this.teacherRepository = teacherRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.getAllTeachers();
    }

    public Teacher getTeacherByTRN(String trn) {
        Optional<Teacher> teacher = teacherRepository.getTeacherByTRN(trn);
        return teacher.orElse(null); 
    }

    public void addTeacher(Teacher teacher) {
        teacherRepository.addTeacher(teacher);
    }

    public int updateTeacher(Teacher teacher) {
        return teacherRepository.updateTeacher(teacher);
    }

    public int deleteTeacher(String trn) {
        return teacherRepository.deleteTeacher(trn);
    }

    public List<Course> getAssignedCourses(String trn) {
        String sql = """
            SELECT c.* 
            FROM Courses c
            JOIN TeacherAssignments ta ON c.course_code = ta.course_code
            WHERE ta.trn = ?
        """;
        
        return jdbcTemplate.query(sql, courseRowMapper, trn);
    }

    public boolean assignCourseToTeacher(String trn, String courseCode) {
        String sql = """
            INSERT INTO TeacherAssignments (trn, course_code)
            SELECT ?, ? 
            FROM dual
            WHERE NOT EXISTS (
                SELECT 1 FROM TeacherAssignments WHERE trn = ? AND course_code = ?
            )
        """;
        
        int rowsAffected = jdbcTemplate.update(sql, trn, courseCode, trn, courseCode);
        return rowsAffected > 0;
    }
}
