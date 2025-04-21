package com.AttendanceManagementSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClassSession {

    private Integer sessionId;
    private String courseCode;
    private String trn;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public ClassSession() {}

    public ClassSession(
        Integer sessionId,
        String courseCode,
        String trn,
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime
    ) {
        this.sessionId = sessionId;
        this.courseCode = courseCode;
        this.trn = trn;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
