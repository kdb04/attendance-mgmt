package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.ClassSession;
import com.AttendanceManagementSystem.model.AttendanceRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionService {

    private final JdbcTemplate jdbcTemplate;

    public SessionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Integer createSession(ClassSession session) {
        // Insert the session and get the auto-generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
            INSERT INTO ClassSessions (
                course_code,
                trn,
                session_date,
                start_time,
                end_time
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, session.getCourseCode());
            ps.setString(2, session.getTrn());
            ps.setDate(3, Date.valueOf(session.getSessionDate()));
            ps.setTime(4, Time.valueOf(session.getStartTime()));
            ps.setTime(5, Time.valueOf(session.getEndTime()));
            return ps;
        }, keyHolder);

        // Get the generated session ID
        Integer sessionId = keyHolder.getKey().intValue();

        // Initialize attendance records for all students in the course
        initializeAttendanceRecords(sessionId, session.getCourseCode());

        return sessionId;
    }

    private void initializeAttendanceRecords(Integer sessionId, String courseCode) {
        String sql = """
            INSERT INTO Attendance (session_id, srn, status, timestamp)
            SELECT 
                ?, 
                se.srn, 
                'absent', 
                NOW()
            FROM 
                StudentEnrollments se
            WHERE 
                se.course_code = ?
        """;

        jdbcTemplate.update(sql, sessionId, courseCode);
    }

    public void updateAttendance(AttendanceRecord record) {
        String sql = """
            UPDATE Attendance
            SET status = ?, timestamp = NOW()
            WHERE session_id = ? AND srn = ?
        """;

        jdbcTemplate.update(
            sql, 
            record.getStatus(),
            record.getSessionId(),
            record.getSrn()
        );
    }

    private final RowMapper<ClassSession> sessionRowMapper = (rs, rowNum) ->
        new ClassSession(
            rs.getInt("session_id"),
            rs.getString("course_code"),
            rs.getString("trn"),
            rs.getDate("session_date").toLocalDate(),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime()
        );

    public List<ClassSession> getSessionsByCourseAndTeacher(String courseCode, String trn) {
        String sql = """
            SELECT * 
            FROM ClassSessions
            WHERE course_code = ? AND trn = ?
            ORDER BY session_date DESC, start_time DESC
        """;
        
        return jdbcTemplate.query(sql, sessionRowMapper, courseCode, trn);
    }

    public List<Map<String, Object>> getAttendanceBySession(Integer sessionId) {
        String sql = """
            SELECT a.srn, a.status, s.name 
            FROM Attendance a
            JOIN Students s ON a.srn = s.srn
            WHERE a.session_id = ?
            ORDER BY s.name
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> attendance = new HashMap<>();
            attendance.put("srn", rs.getString("srn"));
            attendance.put("status", rs.getString("status"));
            attendance.put("name", rs.getString("name"));
            return attendance;
        }, sessionId);
    }
}