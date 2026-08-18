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
        if (attendanceRepository.existsByStudentIdAndCourseIdAndDate(
                studentId,
                courseId,
                LocalDate.now())) {

            throw new RuntimeException(
                    "Attendance already marked for today"
            );
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
    public List<Attendance> getCourseAttendance(Long courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }
    public List<Attendance> getCourseAttendanceByDate(
            Long courseId,
            LocalDate date) {

        return attendanceRepository.findByCourseIdAndDate(
                courseId,
                date
        );
    }
    public AttendanceSummary getAttendanceSummary(
            Long courseId,
            LocalDate date) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int totalStudents = course.getStudents().size();

        List<Attendance> attendanceList =
                attendanceRepository.findByCourseIdAndDate(courseId, date);

        int presentStudents = 0;

        for (Attendance attendance : attendanceList) {
            if (attendance.isPresent()) {
                presentStudents++;
            }
        }

        int absentStudents = totalStudents - presentStudents;

        double attendancePercentage = 0;

        if (totalStudents > 0) {
            attendancePercentage =
                    ((double) presentStudents / totalStudents) * 100;
        }

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
            LocalDate date) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

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
            Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Attendance> attendanceList =
                attendanceRepository.findByCourseId(courseId);

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