package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Professor;
import com.attendance.attendance_system.repository.ProfessorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.attendance.attendance_system.service.JwtService;

import java.util.List;
import java.util.Map;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ProfessorService(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.professorRepository = professorRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService = jwtService;
    }

    public Professor createProfessor(Professor professor) {

        professor.setPassword(
                passwordEncoder.encode(professor.getPassword())
        );

        return professorRepository.save(professor);
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }
    public Map<String, Object> login(
            String email,
            String password) {

        Professor professor = professorRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                password,
                professor.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                professor.getId(),
                professor.getEmail(),
                "PROFESSOR"
        );

        return Map.of(
                "id", professor.getId(),
                "name", professor.getName(),
                "email", professor.getEmail(),
                "role", "PROFESSOR",
                "token", token
        );
    }
}