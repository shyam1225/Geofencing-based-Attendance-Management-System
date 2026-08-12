package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.service.AttendanceService;
import org.springframework.web.bind.annotation.*;
import com.attendance.attendance_system.dto.LocationRequest;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/student/{studentId}/course/{courseId}")
    public Attendance markAttendance(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @RequestBody LocationRequest location) {

        return attendanceService.markAttendance(
                studentId,
                courseId,
                location.getLatitude(),
                location.getLongitude()
        );
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getStudentAttendance(
            @PathVariable Long studentId) {

        return attendanceService.getStudentAttendance(studentId);
    }
}