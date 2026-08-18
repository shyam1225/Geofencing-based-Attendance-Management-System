package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.dto.ProfessorLoginRequest;
import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.entity.Professor;
import com.attendance.attendance_system.service.ProfessorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.attendance.attendance_system.service.CourseService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;
    private final CourseService courseService;

    public ProfessorController(
            ProfessorService professorService,
            CourseService courseService) {

        this.professorService = professorService;
        this.courseService = courseService;
    }

    @PostMapping
    public Professor createProfessor(@RequestBody Professor professor) {
        return professorService.createProfessor(professor);
    }

    @GetMapping
    public List<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }
    @GetMapping("/{professorId}/courses")
    public List<Course> getProfessorCourses(
            @PathVariable Long professorId) {

        return courseService.getProfessorCourses(professorId);
    }
    @GetMapping("/me")
    public String getLoggedInProfessor() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return "Logged in Professor ID: " + principal;
    }
    @GetMapping("/me/courses")
    public List<Course> getMyCourses() {

        Long professorId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return courseService.getProfessorCourses(professorId);
    }
    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody ProfessorLoginRequest request) {

        return professorService.login(
                request.getEmail(),
                request.getPassword()
        );
    }

}