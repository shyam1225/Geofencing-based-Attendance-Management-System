package com.attendance.attendance_system.dto;

import java.time.LocalDate;

public class AttendanceSummary {

    private Long courseId;
    private LocalDate date;
    private int totalStudents;
    private int presentStudents;
    private int absentStudents;
    private double attendancePercentage;

    public AttendanceSummary() {
    }

    public AttendanceSummary(
            Long courseId,
            LocalDate date,
            int totalStudents,
            int presentStudents,
            int absentStudents,
            double attendancePercentage) {

        this.courseId = courseId;
        this.date = date;
        this.totalStudents = totalStudents;
        this.presentStudents = presentStudents;
        this.absentStudents = absentStudents;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getCourseId() {
        return courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public int getPresentStudents() {
        return presentStudents;
    }

    public int getAbsentStudents() {
        return absentStudents;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }
}