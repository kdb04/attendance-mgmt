package com.AttendanceManagementSystem.model;

public class AttendanceRecord {
    private Integer sessionId; // Changed to Integer to match DB type
    private String srn;
    private String status;

    public AttendanceRecord() {
    }

    public AttendanceRecord(Integer sessionId, String srn, String status) {
        this.sessionId = sessionId;
        this.srn = srn;
        this.status = status;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getSrn() {
        return srn;
    }

    public void setSrn(String srn) {
        this.srn = srn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}