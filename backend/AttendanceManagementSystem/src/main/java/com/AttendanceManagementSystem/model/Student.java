package com.AttendanceManagementSystem.model;

public class Student implements User {
    private String srn;
    private String name;
    private int yearOfStudy;

    public Student() {
    }

    public Student(String srn, String name, int yearOfStudy) {
        this.srn = srn;
        this.name = name;
        this.yearOfStudy = yearOfStudy;
    }

    public String getSrn() {
        return srn;
    }

    public void setSrn(String srn) {
        this.srn = srn;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    @Override
    public String getId() {
        return srn;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public String toString() {
        return "Student{" +
                "srn='" + srn + '\'' +
                ", name='" + name + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                '}';
    }
}
