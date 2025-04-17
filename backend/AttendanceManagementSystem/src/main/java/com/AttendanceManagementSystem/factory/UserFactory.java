package com.AttendanceManagementSystem.factory;

import com.AttendanceManagementSystem.model.*;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    
    public User createUser(String userType, String... params) {
        return switch (userType.toUpperCase()) {
            case "STUDENT" -> {
                validateStudentParams(params);
                Student student = new Student(params[0], params[1], Integer.parseInt(params[2]));
                yield student;
            }
            case "TEACHER" -> {
                validateTeacherParams(params);
                Teacher teacher = new Teacher(params[0], params[1]);
                yield teacher;
            }
            case "ADMIN" -> new Admin();
            default -> throw new IllegalArgumentException("Invalid user type: " + userType);
        };
    }

    private void validateStudentParams(String[] params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("Student requires SRN, name, and year of study");
        }
        if (!params[0].matches("PES2UG\\d{2}CS\\d{3}")) {
            throw new IllegalArgumentException("Invalid SRN format");
        }
        try {
            int year = Integer.parseInt(params[2]);
            if (year < 1 || year > 4) {
                throw new IllegalArgumentException("Year of study must be between 1 and 4");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year of study");
        }
    }

    private void validateTeacherParams(String[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Teacher requires TRN and name");
        }
        if (!params[0].matches("TRN\\d{3}")) {
            throw new IllegalArgumentException("Invalid TRN format");
        }
    }
}