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

--Trigger to populate StudentEnrollments
DELIMITER //
CREATE TRIGGER after_students_insert
AFTER INSERT ON Students
FOR EACH ROW
BEGIN
    DECLARE course_limit INT;
    SET course_limit = (SELECT LEAST(COUNT(*), 5) FROM Courses);

    INSERT INTO StudentEnrollments (srn, course_code)
    SELECT NEW.srn, c.course_code
    FROM Courses c
    WHERE NOT EXISTS (
        SELECT 1 FROM StudentEnrollments se
        WHERE se.srn = NEW.srn AND se.course_code = c.course_code
    )
    ORDER BY RAND()
    LIMIT course_limit;
END //
DELIMITER ;

-- Trigger to populate ClassSessions
DELIMITER //
CREATE TRIGGER after_teacher_assignment
AFTER INSERT ON TeacherAssignments
FOR EACH ROW
BEGIN
    -- Create class sessions for the next 30 days with fixed time slots
    INSERT INTO ClassSessions (course_code, trn, session_date, start_time, end_time)
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
    WHERE WEEKDAY(d.date) BETWEEN 0 AND 4
    AND NOT EXISTS (
        SELECT 1 FROM ClassSessions cs
        WHERE cs.course_code = NEW.course_code
        AND cs.session_date = d.date
        AND cs.start_time = CASE DAYOFWEEK(d.date)
            WHEN 2 THEN '09:00:00'
            WHEN 3 THEN '11:00:00'
            WHEN 4 THEN '14:00:00'
            WHEN 5 THEN '10:00:00'
            WHEN 6 THEN '13:00:00'
        END
    );
END //
DELIMITER ;


--  Trigger to update Attendance
DELIMITER //
CREATE TRIGGER after_student_enrollment
AFTER INSERT ON StudentEnrollments
FOR EACH ROW
BEGIN
    INSERT INTO Attendance (session_id, srn, status, timestamp)
    SELECT cs.session_id, NEW.srn, 'absent', NOW()
    FROM ClassSessions cs
    WHERE cs.course_code = NEW.course_code
    AND NOT EXISTS (
        SELECT 1 FROM Attendance a
        WHERE a.session_id = cs.session_id AND a.srn = NEW.srn
    );
END //
DELIMITER ;

