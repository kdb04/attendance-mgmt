package com.AttendanceManagementSystem.model;

public class Admin implements User {
    private String id;
    private String name;
    private String role;

    public Admin() {
        this.id = "admin";
        this.name = "Admin User";
        this.role = "ADMIN";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRole() {
        return role;
    }
}