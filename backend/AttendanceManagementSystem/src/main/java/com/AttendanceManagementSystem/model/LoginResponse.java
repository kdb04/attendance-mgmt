package com.AttendanceManagementSystem.model;

public class LoginResponse {

    private String userId;
    private String role;
    private String name;
    private String message;

    public LoginResponse(
        String userId,
        String role,
        String name,
        String message
    ) {
        this.userId = userId;
        this.role = role;
        this.name = name;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
