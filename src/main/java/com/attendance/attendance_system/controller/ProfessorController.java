package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.entity.Professor;
import com.attendance.attendance_system.service.ProfessorService;
import org.springframework.web.bind.annotation.*;
import com.attendance.attendance_system.service.CourseService;
import java.util.List;

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

        return courseService.getCoursesByProfessor(professorId);
    }

}