package com.drivinglicense.repository;

import com.drivinglicense.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    long countByAppointmentDate(LocalDate date);

    @Query("SELECT COUNT(e) FROM VisionExam e WHERE e.appointmentDate = :date")
    long countVisionByAppointmentDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM TheoryExam e WHERE e.appointmentDate = :date")
    long countTheoryByAppointmentDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM PracticalExam e WHERE e.appointmentDate = :date")
    long countPracticalByAppointmentDate(@Param("date") LocalDate date);

    List<Exam> findByRequest_IdOrderByIdAsc(Long requestId);

    @Query("SELECT e FROM Exam e WHERE " +
            "(:appointmentDate IS NULL OR e.appointmentDate = :appointmentDate) AND " +
            "(:examType IS NULL OR TYPE(e) = :examType) AND " +
            "(:pendingOnly = false OR e.result IS NULL)")
    Page<Exam> filter(@Param("appointmentDate") LocalDate appointmentDate,
                      @Param("examType") Class<? extends Exam> examType,
                      @Param("pendingOnly") boolean pendingOnly,
                      Pageable pageable);
}