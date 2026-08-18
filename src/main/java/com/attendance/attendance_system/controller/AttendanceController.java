package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.dto.AttendanceRecordDTO;
import com.attendance.attendance_system.dto.StudentAttendanceDTO;
import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.service.AttendanceService;
import org.springframework.web.bind.annotation.*;
import com.attendance.attendance_system.dto.LocationRequest;
import java.time.LocalDate;
import java.util.List;
import com.attendance.attendance_system.dto.AttendanceSummary;

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
    @GetMapping("/course/{courseId}")
    public List<Attendance> getCourseAttendance(
            @PathVariable Long courseId) {

        return attendanceService.getCourseAttendance(courseId);
    }
    @GetMapping("/course/{courseId}/date/{date}")
    public List<Attendance> getCourseAttendanceByDate(
            @PathVariable Long courseId,
            @PathVariable LocalDate date) {

        return attendanceService.getCourseAttendanceByDate(
                courseId,
                date
        );
    }
    @GetMapping("/course/{courseId}/summary/{date}")
    public AttendanceSummary getAttendanceSummary(
            @PathVariable Long courseId,
            @PathVariable LocalDate date) {

        return attendanceService.getAttendanceSummary(
                courseId,
                date
        );
    }
    @GetMapping("/course/{courseId}/records/{date}")
    public List<AttendanceRecordDTO> getAttendanceRecords(
            @PathVariable Long courseId,
            @PathVariable LocalDate date) {

        return attendanceService.getAttendanceRecords(
                courseId,
                date
        );
    }
    @GetMapping("/course/{courseId}/overall")
    public List<StudentAttendanceDTO> getOverallAttendance(
            @PathVariable Long courseId) {

        return attendanceService.getOverallAttendance(courseId);
    }
}