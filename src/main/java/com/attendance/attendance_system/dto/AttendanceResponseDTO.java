package com.attendance.attendance_system.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceResponseDTO {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private boolean present;

    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String rollNumber;

    private Long courseId;
    private String courseName;

    public AttendanceResponseDTO(
            Long id,
            LocalDate date,
            LocalTime time,
            boolean present,
            Long studentId,
            String studentName,
            String studentEmail,
            String rollNumber,
            Long courseId,
            String courseName) {

        this.id = id;
        this.date = date;
        this.time = time;
        this.present = present;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.rollNumber = rollNumber;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public boolean isPresent() {
        return present;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }
}