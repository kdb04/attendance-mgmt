package com.AttendanceManagementSystem.service;

import com.AttendanceManagementSystem.model.Student;
import com.AttendanceManagementSystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) { //dependency injection
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public Student getStudentById(String srn) {
        Optional<Student> student = studentRepository.getStudentById(srn);
        return student.orElse(null); // Return null if student not found
    }

    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }

    public int updateStudent(Student student) {
        return studentRepository.updateStudent(student);
    }

    public int deleteStudent(String srn) {
        return studentRepository.deleteStudent(srn);
    }
}
