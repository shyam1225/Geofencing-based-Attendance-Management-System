package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.entity.Professor;
import com.attendance.attendance_system.repository.CourseRepository;
import com.attendance.attendance_system.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.StudentRepository;


import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    public CourseService(
            CourseRepository courseRepository,
            ProfessorRepository professorRepository,
            StudentRepository studentRepository) {

        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
    }

    public Course createCourse(Course course) {

        if (course.getProfessor() != null) {

            Long professorId = course.getProfessor().getId();

            Professor professor = professorRepository
                    .findById(professorId)
                    .orElseThrow(() ->
                            new RuntimeException("Professor not found"));

            course.setProfessor(professor);
        }

        return courseRepository.save(course);
    }
    public Course addStudentToCourse(Long courseId, Long studentId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        course.getStudents().add(student);

        return courseRepository.save(course);
    }
    public void deleteCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        courseRepository.delete(course);
    }
    public Course assignProfessor(Long courseId, Long professorId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        course.setProfessor(professor);

        return courseRepository.save(course);
    }
    public List<Course> getProfessorCourses(Long professorId) {

        return courseRepository.findByProfessorId(professorId);
    }
    public List<Student> getCourseStudents(
            Long courseId,
            Long professorId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        if (course.getProfessor() == null ||
                !course.getProfessor().getId().equals(professorId)) {

            throw new RuntimeException(
                    "You are not the professor of this course");
        }

        return course.getStudents();
    }
}
