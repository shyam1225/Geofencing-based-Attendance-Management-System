package com.attendance.attendance_system.dto;

public class StudentAttendanceDTO {

    private Long studentId;
    private String studentName;
    private String rollNumber;
    private double attendancePercentage;

    public StudentAttendanceDTO(
            Long studentId,
            String studentName,
            String rollNumber,
            double attendancePercentage) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }
}