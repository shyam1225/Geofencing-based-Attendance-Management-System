package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.dto.*;
import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.service.AttendanceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;import com.attendance.attendance_system.dto.AttendanceResponseDTO;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/course/{courseId}")
    public AttendanceResponseDTO markAttendance(
            @PathVariable Long courseId,
            @RequestBody LocationRequest location) {

        Long studentId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

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
    @GetMapping("/course/{courseId}/summary/{date}")
    public AttendanceSummary getAttendanceSummary(
            @PathVariable Long courseId,
            @PathVariable LocalDate date) {

        Long professorId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return attendanceService.getAttendanceSummary(
                courseId,
                date,
                professorId
        );
    }
    @GetMapping("/course/{courseId}/records/{date}")
    public List<AttendanceRecordDTO> getAttendanceRecords(
            @PathVariable Long courseId,
            @PathVariable LocalDate date) {
        Long professorId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return attendanceService.getAttendanceRecords(
                courseId,
                date,
                professorId
        );
    }
    @GetMapping("/course/{courseId}/overall")
    public List<StudentAttendanceDTO> getOverallAttendance(
            @PathVariable Long courseId) {

        Long professorId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return attendanceService.getOverallAttendance(
                courseId,
                professorId
        );
    }
}