package com.AttendanceManagementSystem.model;

public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private int totalClasses;

    public Course(){
    }

    public Course(String courseCode, String courseName, int credits){
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        // Set totalClasses based on credits
        this.setCredits(credits);
    }

    // This constructor might be needed for handling DB queries that include totalClasses
    public Course(String courseCode, String courseName, int credits, int totalClasses){
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.totalClasses = totalClasses;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public int getCredits(){
        return credits;
    }

    public void setCredits(int credits){
        this.credits = credits;
        // Auto-set totalClasses when credits are set
        switch (credits) {
            case 2: this.totalClasses = 20; break;
            case 3: this.totalClasses = 40; break;
            case 4: this.totalClasses = 60; break;
            case 5: this.totalClasses = 80; break;
            case 6: this.totalClasses = 100; break;
            default: this.totalClasses = 40; // default fallback
        }
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    @Override
    public String toString(){
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", totalClasses=" + totalClasses +
                '}';
    }
}
