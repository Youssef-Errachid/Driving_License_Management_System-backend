package com.drivinglicense.repository;

import com.drivinglicense.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    long countByAppointmentDate(LocalDate date);

    @Query("SELECT COUNT(e) FROM VisionExam e WHERE e.appointmentDate = :date")
    long countVisionByAppointmentDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM TheoryExam e WHERE e.appointmentDate = :date")
    long countTheoryByAppointmentDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM PracticalExam e WHERE e.appointmentDate = :date")
    long countPracticalByAppointmentDate(@Param("date") LocalDate date);
}