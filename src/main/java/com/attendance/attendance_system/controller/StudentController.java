package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.dto.StudentLoginRequest;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.attendance.attendance_system.entity.Course;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody StudentLoginRequest request) {

        return studentService.login(
                request.getEmail(),
                request.getPassword()
        );
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}/courses")
    public List<Course> getStudentCourses(
            @PathVariable Long studentId) {

        return studentService.getStudentCourses(studentId);
    }

}