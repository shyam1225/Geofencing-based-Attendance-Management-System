package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}