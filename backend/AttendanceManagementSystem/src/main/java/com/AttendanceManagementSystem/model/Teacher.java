package com.AttendanceManagementSystem.model;

public class Teacher {
    private String trn;
    private String name;

    public Teacher(){
    }

    public Teacher(String trn, String name){
        this.trn = trn;
        this.name = name;
    }

    public String getTRN(){
        return trn;
    }

    public void setTRN(String trn){
        this.trn = trn;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Teacher{" +
                "trn='" + trn + '\'' +
                ", name" + name +
                '}';
    }
}
