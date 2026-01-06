package com.AttendanceManagementSystem.facade;

import com.AttendanceManagementSystem.model.LoginResponse;

public interface LoginFacade {
    LoginResponse adminLogin(String username, String password);
    LoginResponse studentLogin(String username, String password);
    LoginResponse teacherLogin(String username, String password);
}