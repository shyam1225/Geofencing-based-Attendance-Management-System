package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.service.CourseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.attendance.attendance_system.entity.Student;


import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public Course addStudentToCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {

        return courseService.addStudentToCourse(courseId, studentId);
    }
    @DeleteMapping("/{courseId}")
    public String deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return "Course deleted successfully";
    }
    @PutMapping("/{courseId}/professor/{professorId}")
    public Course assignProfessor(
            @PathVariable Long courseId,
            @PathVariable Long professorId) {

        return courseService.assignProfessor(courseId, professorId);
    }
    @GetMapping("/professor/{professorId}")
    public List<Course> getProfessorCourses(
            @PathVariable Long professorId) {

        return courseService.getProfessorCourses(professorId);
    }
    @GetMapping("/{courseId}/students")
    public List<Student> getCourseStudents(
            @PathVariable Long courseId) {

        Long professorId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return courseService.getCourseStudents(
                courseId,
                professorId
        );
    }

}