package com.AttendanceManagementSystem.facade;

import com.AttendanceManagementSystem.model.LoginResponse;
import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.service.StudentService;
import com.AttendanceManagementSystem.service.TeacherService;
import org.springframework.stereotype.Component;

@Component
public class LoginFacadeImpl implements LoginFacade {
    
    private final StudentService studentService;
    private final TeacherService teacherService;
    
    public LoginFacadeImpl(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    public LoginResponse adminLogin(String username, String password) {
        if ("admin".equals(username) && "admin123#".equals(password)) {
            return new LoginResponse(
                "admin",
                "ADMIN",
                "Admin user",
                "Login successful",
                null
            );
        }
        return null;
    }

    @Override
    public LoginResponse studentLogin(String username) {
        Student student = studentService.getStudentById(username);
        if (student != null) {
            return new LoginResponse(
                username,
                "STUDENT",
                student.getName(),
                "Login successful",
                null
            );
        }
        return null;
    }

    @Override
    public LoginResponse teacherLogin(String username) {
        Teacher teacher = teacherService.getTeacherByTRN(username);
        if (teacher != null) {
            return new LoginResponse(
                username,
                "TEACHER",
                teacher.getName(),
                "Login successful",
                null
            );
        }
        return null;
    }
}