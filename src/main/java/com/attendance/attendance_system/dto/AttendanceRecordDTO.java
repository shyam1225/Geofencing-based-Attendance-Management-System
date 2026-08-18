package com.attendance.attendance_system.dto;

public class AttendanceRecordDTO {

    private Long studentId;
    private String studentName;
    private String rollNumber;
    private boolean present;

    public AttendanceRecordDTO(
            Long studentId,
            String studentName,
            String rollNumber,
            boolean present) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.present = present;
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

    public boolean isPresent() {
        return present;
    }
}