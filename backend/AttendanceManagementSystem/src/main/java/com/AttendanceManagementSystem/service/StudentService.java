package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Course;
import com.AttendanceManagementSystem.repository.StudentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public Map<String, Object> getAttendanceStats(String srn, String courseCode) {
        String sql = """
            WITH CourseDetails AS (
                SELECT 
                    course_code, 
                    total_classes,
                    CEILING(0.75 * total_classes) AS min_sessions_required
                FROM 
                    Courses
                WHERE 
                    course_code = ?
            ),
            SessionsHeld AS (
                SELECT 
                    COUNT(*) as total_sessions_held
                FROM 
                    ClassSessions
                WHERE 
                    course_code = ?
            ),
            StudentAttendance AS (
                SELECT 
                    COUNT(*) as sessions_attended
                FROM 
                    Attendance a
                JOIN 
                    ClassSessions cs ON a.session_id = cs.session_id
                WHERE 
                    a.srn = ?
                    AND cs.course_code = ?
                    AND a.status = 'present'
            )
            SELECT
                cd.course_code,
                cd.total_classes AS total_classes_for_course,
                sh.total_sessions_held AS classes_held_so_far,
                cd.min_sessions_required,
                sa.sessions_attended AS current_attendance,
                CASE 
                    WHEN sh.total_sessions_held = 0 THEN 0
                    ELSE ROUND((sa.sessions_attended * 100.0) / sh.total_sessions_held, 2)
                END AS current_attendance_percentage,
                CASE 
                    WHEN sa.sessions_attended >= cd.min_sessions_required THEN 0
                    ELSE cd.min_sessions_required - sa.sessions_attended
                END AS additional_sessions_needed
            FROM 
                CourseDetails cd
            CROSS JOIN 
                SessionsHeld sh
            CROSS JOIN 
                StudentAttendance sa
        """;

        return jdbcTemplate.queryForMap(sql, courseCode, courseCode, srn, courseCode);
    }
}
