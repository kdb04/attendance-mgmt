CREATE DATABASE attendance_db;

-- Students Table
CREATE TABLE Students (
    srn VARCHAR(20) PRIMARY KEY,
    name TEXT NOT NULL,
    year_of_study INT NOT NULL CHECK (year_of_study BETWEEN 1 AND 4),
    password TEXT
);

-- Teachers Table
CREATE TABLE Teachers (
    trn VARCHAR(20) PRIMARY KEY,
    name TEXT NOT NULL,
    password TEXT
);

-- Courses Table
CREATE TABLE Courses (
    course_code VARCHAR(20) PRIMARY KEY,
    course_name TEXT NOT NULL,
    credits INT NOT NULL CHECK (credits BETWEEN 2 AND 6),
    total_classes INT NOT NULL
);

-- Student Enrollments Table
CREATE TABLE StudentEnrollments (
    srn VARCHAR(20) REFERENCES Students(srn) ON DELETE CASCADE,
    course_code VARCHAR(20) REFERENCES Courses(course_code) ON DELETE CASCADE,
    enrollment_date DATE NOT NULL,
    PRIMARY KEY (srn, course_code)
);

-- Teacher Assignments Table
CREATE TABLE TeacherAssignments (
    trn VARCHAR(20) REFERENCES Teachers(trn) ON DELETE CASCADE,
    course_code VARCHAR(20) REFERENCES Courses(course_code) ON DELETE CASCADE,
    PRIMARY KEY (trn, course_code)
);

-- Class Sessions Table
CREATE TABLE ClassSessions (
    session_id SERIAL PRIMARY KEY,
    course_code VARCHAR(20) REFERENCES Courses(course_code) ON DELETE CASCADE,
    trn VARCHAR(20) REFERENCES Teachers(trn) ON DELETE CASCADE,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- Attendance Table
CREATE TABLE Attendance (
    attendance_id SERIAL PRIMARY KEY,
    session_id INT NOT NULL,
    srn VARCHAR(20) NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('present', 'absent', 'deceased')),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attendance_session
        FOREIGN KEY (session_id)
        REFERENCES ClassSessions(session_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_attendance_student
        FOREIGN KEY (srn)
        REFERENCES Students(srn)
        ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_attendance_session ON Attendance(session_id);
CREATE INDEX idx_attendance_student ON Attendance(srn);
CREATE INDEX idx_sessions_course ON ClassSessions(course_code);
CREATE INDEX idx_enrollments_student ON StudentEnrollments(srn);
CREATE INDEX idx_enrollments_course ON StudentEnrollments(course_code);

-- Trigger - auto assign courses to teachers with lesser than 3 courses assigned
-- function
CREATE OR REPLACE FUNCTION after_courses_insert_fn()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO TeacherAssignments (trn, course_code)
    SELECT t.trn, NEW.course_code
    FROM Teachers t
    LEFT JOIN (
        SELECT trn, COUNT(*) AS course_count
        FROM TeacherAssignments
        GROUP BY trn
    ) ta ON t.trn = ta.trn
    WHERE ta.trn IS NULL OR ta.course_count < 3
    ORDER BY COALESCE(ta.course_count, 0)
    LIMIT 2;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
-- trigger
CREATE TRIGGER after_courses_insert
AFTER INSERT ON Courses
FOR EACH ROW
EXECUTE FUNCTION after_courses_insert_fn();

-- Trigger - auto create sessions for a course
-- function
CREATE OR REPLACE FUNCTION after_teacher_assignment_fn()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO ClassSessions (
        course_code,
        trn,
        session_date,
        start_time,
        end_time
    )
    WITH RECURSIVE dates AS (
        SELECT CURRENT_DATE AS d
        UNION ALL
        SELECT d + INTERVAL '1 day'
        FROM dates
        WHERE d < CURRENT_DATE + INTERVAL '30 days'
    )
    SELECT DISTINCT
        NEW.course_code,
        NEW.trn,
        d::DATE,
        CASE EXTRACT(DOW FROM d)
            WHEN 1 THEN TIME '09:00'
            WHEN 2 THEN TIME '11:00'
            WHEN 3 THEN TIME '14:00'
            WHEN 4 THEN TIME '10:00'
            WHEN 5 THEN TIME '13:00'
        END AS start_time,
        CASE EXTRACT(DOW FROM d)
            WHEN 1 THEN TIME '11:00'
            WHEN 2 THEN TIME '13:00'
            WHEN 3 THEN TIME '16:00'
            WHEN 4 THEN TIME '12:00'
            WHEN 5 THEN TIME '15:00'
        END AS end_time
    FROM dates
    WHERE EXTRACT(DOW FROM d) BETWEEN 1 AND 5
      AND NOT EXISTS (
        SELECT 1
        FROM ClassSessions cs
        WHERE cs.course_code = NEW.course_code
          AND cs.session_date = d::DATE
          AND cs.start_time =
            CASE EXTRACT(DOW FROM d)
                WHEN 1 THEN TIME '09:00'
                WHEN 2 THEN TIME '11:00'
                WHEN 3 THEN TIME '14:00'
                WHEN 4 THEN TIME '10:00'
                WHEN 5 THEN TIME '13:00'
            END
      );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
-- trigger
CREATE TRIGGER after_teacher_assignment
AFTER INSERT ON TeacherAssignments
FOR EACH ROW
EXECUTE FUNCTION after_teacher_assignment_fn();

-- To prevent duplicate attendance records
ALTER TABLE Attendance
ADD CONSTRAINT unique_attendance_per_session
UNIQUE (session_id, srn);
