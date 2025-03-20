package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Teacher;
import com.AttendanceManagementSystem.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.getAllTeachers();
    }

    public Teacher getTeacherByTRN(String trn) {
        Optional<Teacher> teacher = teacherRepository.getTeacherByTRN(trn);
        return teacher.orElse(null); 
    }

    public void addTeacher(Teacher teacher) {
        teacherRepository.addTeacher(teacher);
    }

    public int updateTeacher(Teacher teacher) {
        return teacherRepository.updateTeacher(teacher);
    }

    public int deleteTeacher(String trn) {
        return teacherRepository.deleteTeacher(trn);
    }
}
