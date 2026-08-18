package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseIdAndDate(
            Long studentId,
            Long courseId,
            LocalDate date
    );
    List<Attendance> findByCourseIdAndDate(
            Long courseId,
            LocalDate date
    );

}