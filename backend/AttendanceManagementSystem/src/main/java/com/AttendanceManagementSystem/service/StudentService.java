package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.repository.StudentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Course> courseRowMapper = (rs, rowNum) ->
        new Course(
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("credits")
        );

    public StudentService(StudentRepository studentRepository, JdbcTemplate jdbcTemplate) { //dependency injection
        this.studentRepository = studentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public Student getStudentById(String srn) {
        Optional<Student> student = studentRepository.getStudentById(srn);
        return student.orElse(null); // Return null if student not found
    }

    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }

    public int updateStudent(Student student) {
        return studentRepository.updateStudent(student);
    }

    public int deleteStudent(String srn) {
        return studentRepository.deleteStudent(srn);
    }

    public List<Course> getEnrolledCourses(String srn) {
        String sql = """
            SELECT c.* 
            FROM Courses c
            JOIN StudentEnrollments se ON c.course_code = se.course_code
            WHERE se.srn = ?
        """;

        return jdbcTemplate.query(sql, courseRowMapper, srn);
    }
}
