package com.AttendanceManagementSystem.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping
    public String test() {
        return "API is working";
    }
}