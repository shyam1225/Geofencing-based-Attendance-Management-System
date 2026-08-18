package com.attendance.attendance_system.dto;

public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String rollNumber;

    public StudentResponse(
            Long id,
            String name,
            String email,
            String rollNumber) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.rollNumber = rollNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRollNumber() {
        return rollNumber;
    }
}