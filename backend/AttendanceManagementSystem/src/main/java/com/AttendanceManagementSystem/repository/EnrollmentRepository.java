package com.AttendanceManagementSystem.repository;

import com.AttendanceManagementSystem.util.DBConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public EnrollmentRepository() {
        this.jdbcTemplate = DBConnection.getInstance().getJdbcTemplate();
    }

    public int enrollStudent(String srn, String courseCode) {
        String sql = """
            INSERT INTO StudentEnrollments (srn, course_code, enrollment_date)
            VALUES (?, ?, CURRENT_DATE)
        """;
        return jdbcTemplate.update(sql, srn, courseCode);
    }

    public boolean isAlreadyEnrolled(String srn, String courseCode) {
        String sql = """
            SELECT COUNT(*)
            FROM StudentEnrollments
            WHERE srn = ? AND course_code = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, srn, courseCode);
        return count != null && count > 0;
    }
}
