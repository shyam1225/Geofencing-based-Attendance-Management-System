package com.attendance.attendance_system.dto;

public class ProfessorLoginRequest {

    private String email;
    private String password;

    public ProfessorLoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}