package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}