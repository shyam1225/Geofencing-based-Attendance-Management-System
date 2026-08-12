package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.AttendanceRepository;
import com.attendance.attendance_system.repository.CourseRepository;
import com.attendance.attendance_system.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.attendance.attendance_system.service.GeofenceService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GeofenceService geofenceService;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            GeofenceService geofenceService) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.geofenceService = geofenceService;
    }

    public Attendance markAttendance(
            Long studentId,
            Long courseId,
            double studentLatitude,
            double studentLongitude) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check enrollment
        if (!course.getStudents().contains(student)) {
            throw new RuntimeException(
                    "Student is not enrolled in this course");
        }

        // Check geofence
        boolean insideGeofence = geofenceService.isInsideGeofence(
                studentLatitude,
                studentLongitude,
                course.getLatitude(),
                course.getLongitude(),
                course.getRadius()
        );

        if (!insideGeofence) {
            throw new RuntimeException(
                    "You are outside the attendance location");
        }

        Attendance attendance = new Attendance();

        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());
        attendance.setPresent(true);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}