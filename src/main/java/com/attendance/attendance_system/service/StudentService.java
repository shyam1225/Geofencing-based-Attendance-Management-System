package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.List;
import com.attendance.attendance_system.entity.Course;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public List<Course> getStudentCourses(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getCourses();
    }
}