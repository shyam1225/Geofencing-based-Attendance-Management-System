package com.attendance.attendance_system.service;

import com.attendance.attendance_system.dto.AttendanceRecordDTO;
import com.attendance.attendance_system.dto.StudentAttendanceDTO;
import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.entity.Course;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.AttendanceRepository;
import com.attendance.attendance_system.repository.CourseRepository;
import com.attendance.attendance_system.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.attendance.attendance_system.dto.AttendanceSummary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.attendance.attendance_system.dto.AttendanceResponseDTO;
import com.attendance.attendance_system.dto.AttendanceResponseDTO;
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

    public AttendanceResponseDTO markAttendance(
            Long studentId,
            Long courseId,
            double studentLatitude,
            double studentLongitude) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        // Check enrollment
        if (!course.getStudents().contains(student)) {
            throw new RuntimeException(
                    "Student is not enrolled in this course");
        }

        // Check duplicate attendance
        if (attendanceRepository.existsByStudentIdAndCourseIdAndDate(
                studentId,
                courseId,
                LocalDate.now())) {

            throw new RuntimeException(
                    "Attendance already marked for today");
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

        // Create attendance
        Attendance attendance = new Attendance();

        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());
        attendance.setPresent(true);

        // Save attendance
        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        // Return DTO instead of entity
        return new AttendanceResponseDTO(
                savedAttendance.getId(),
                savedAttendance.getDate(),
                savedAttendance.getTime(),
                savedAttendance.isPresent(),
                savedAttendance.getStudent().getId(),
                savedAttendance.getStudent().getName(),
                savedAttendance.getStudent().getEmail(),
                savedAttendance.getStudent().getRollNumber(),
                savedAttendance.getCourse().getId(),
                savedAttendance.getCourse().getName()
        );
    }

    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    public AttendanceSummary getAttendanceSummary(
            Long courseId,
            LocalDate date,
            Long professorId) {

        // Find the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        // Make sure the logged-in professor owns this course
        if (course.getProfessor() == null ||
                !course.getProfessor().getId().equals(professorId)) {

            throw new RuntimeException(
                    "You are not the professor of this course");
        }

        // Get all attendance records for this course and date
        List<Attendance> records =
                attendanceRepository.findByCourseIdAndDate(
                        courseId,
                        date
                );

        // Total students enrolled in the course
        int totalStudents = course.getStudents().size();

        // Count present students
        int presentStudents = (int) records.stream()
                .filter(Attendance::isPresent)
                .count();

        // Calculate absent students
        int absentStudents = totalStudents - presentStudents;

        // Calculate percentage
        double attendancePercentage = totalStudents == 0
                ? 0.0
                : (presentStudents * 100.0) / totalStudents;

        // Return summary
        return new AttendanceSummary(
                courseId,
                date,
                totalStudents,
                presentStudents,
                absentStudents,
                attendancePercentage
        );
    }
    public List<AttendanceRecordDTO> getAttendanceRecords(
            Long courseId,
            LocalDate date,
            Long professorId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        if (course.getProfessor() == null ||
                !course.getProfessor().getId().equals(professorId)) {

            throw new RuntimeException(
                    "You are not the professor of this course");
        }

        List<Attendance> attendanceList =
                attendanceRepository.findByCourseIdAndDate(
                        courseId,
                        date
                );

        return course.getStudents().stream()
                .map(student -> {

                    boolean present = attendanceList.stream()
                            .anyMatch(attendance ->
                                    attendance.getStudent().getId()
                                            .equals(student.getId())
                            );

                    return new AttendanceRecordDTO(
                            student.getId(),
                            student.getName(),
                            student.getRollNumber(),
                            present
                    );
                })
                .toList();
    }
    public List<StudentAttendanceDTO> getOverallAttendance(
            Long courseId,
            Long professorId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Attendance> attendanceList =
                attendanceRepository.findByCourseId(courseId);
        if (course.getProfessor() == null ||
                !course.getProfessor().getId().equals(professorId)) {

            throw new RuntimeException(
                    "You are not the professor of this course");
        }

        // Find all dates on which attendance was conducted
        Set<LocalDate> attendanceDates = attendanceList.stream()
                .map(Attendance::getDate)
                .collect(Collectors.toSet());

        int totalClasses = attendanceDates.size();

        return course.getStudents().stream()
                .map(student -> {

                    long presentClasses = attendanceList.stream()
                            .filter(attendance ->
                                    attendance.getStudent()
                                            .getId()
                                            .equals(student.getId()))
                            .map(Attendance::getDate)
                            .distinct()
                            .count();

                    double percentage = totalClasses == 0
                            ? 0.0
                            : (presentClasses * 100.0) / totalClasses;

                    return new StudentAttendanceDTO(
                            student.getId(),
                            student.getName(),
                            student.getRollNumber(),
                            percentage
                    );
                })
                .toList();
    }
}