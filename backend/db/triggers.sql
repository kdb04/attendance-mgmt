-- Trigger to populate TeacherAssignments
DELIMITER //
CREATE TRIGGER after_courses_insert
AFTER INSERT ON Courses
FOR EACH ROW
BEGIN
    -- Assign 2 teachers who have the least number of course assignments
    INSERT INTO TeacherAssignments (trn, course_code)
    SELECT t.trn, NEW.course_code
    FROM Teachers t
    LEFT JOIN (
        SELECT trn, COUNT(*) as course_count
        FROM TeacherAssignments
        GROUP BY trn
    ) ta ON t.trn = ta.trn
    WHERE ta.trn IS NULL OR ta.course_count < 3
    ORDER BY COALESCE(ta.course_count, 0)
    LIMIT 2;
END //
DELIMITER ;

-- Trigger to populate StudentEnrollments
DELIMITER //
CREATE TRIGGER after_students_insert
AFTER INSERT ON Students
FOR EACH ROW
BEGIN
    -- Enroll each student in 5 courses
    INSERT INTO StudentEnrollments (srn, course_code)
    SELECT NEW.srn, c.course_code
    FROM Courses c
    WHERE c.course_code NOT IN (
        SELECT se.course_code
        FROM StudentEnrollments se
        WHERE se.srn = NEW.srn
    );
END //
DELIMITER ;

-- Trigger to populate ClassSessions
DELIMITER //
CREATE TRIGGER after_teacher_assignment
AFTER INSERT ON TeacherAssignments
FOR EACH ROW
BEGIN
    -- Create class sessions for the next 30 days with fixed time slots
    INSERT IGNORE INTO ClassSessions (course_code, trn, session_date, start_time, end_time)
    WITH RECURSIVE dates AS (
        SELECT CURDATE() as date
        UNION ALL
        SELECT date + INTERVAL 1 DAY
        FROM dates
        WHERE date < CURDATE() + INTERVAL 30 DAY
    )
    SELECT DISTINCT
        NEW.course_code,
        NEW.trn,
        d.date,
        CASE DAYOFWEEK(d.date)
            WHEN 2 THEN '09:00:00' -- Monday
            WHEN 3 THEN '11:00:00' -- Tuesday
            WHEN 4 THEN '14:00:00' -- Wednesday
            WHEN 5 THEN '10:00:00' -- Thursday
            WHEN 6 THEN '13:00:00' -- Friday
        END as start_time,
        CASE DAYOFWEEK(d.date)
            WHEN 2 THEN '11:00:00' -- Monday
            WHEN 3 THEN '13:00:00' -- Tuesday
            WHEN 4 THEN '16:00:00' -- Wednesday
            WHEN 5 THEN '12:00:00' -- Thursday
            WHEN 6 THEN '15:00:00' -- Friday
        END as end_time
    FROM dates d
    WHERE WEEKDAY(d.date) BETWEEN 0 AND 4;  -- Monday to Friday only
END //
DELIMITER ;

-- Trigger to populate Attendance
DELIMITER //
CREATE TRIGGER after_class_session
AFTER INSERT ON ClassSessions
FOR EACH ROW
BEGIN
    -- Record attendance for all students enrolled in the course
    INSERT INTO Attendance (session_id, srn, status, timestamp)
    SELECT
        NEW.session_id,
        se.srn,
        'present' as status,
        NOW() as timestamp
    FROM StudentEnrollments se
    WHERE se.course_code = NEW.course_code;
END //
DELIMITER ;
