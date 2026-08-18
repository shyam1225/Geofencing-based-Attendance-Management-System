package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public StudentService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Student createStudent(Student student) {

        student.setPassword(
                passwordEncoder.encode(student.getPassword())
        );

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Course> getStudentCourses(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return student.getCourses();
    }

    public Map<String,Object> login(String email, String password) {

        Student student = studentRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                password,
                student.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(
                student.getId(),
                student.getEmail(),
                "STUDENT"
        );

        return Map.of(
                "id", student.getId(),
                "name", student.getName(),
                "email", student.getEmail(),
                "role", "STUDENT",
                "token", token
        );
    }
}