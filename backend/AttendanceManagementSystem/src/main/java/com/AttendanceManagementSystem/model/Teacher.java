package com.AttendanceManagementSystem.model;

public class Teacher implements User {
    private String trn;
    private String name;

    public Teacher() {
    }

    public Teacher(String trn, String name) {
        this.trn = trn;
        this.name = name;
    }

    public String getTRN() {
        return trn;
    }

    public void setTRN(String trn) {
        this.trn = trn;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return trn;
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }
}
