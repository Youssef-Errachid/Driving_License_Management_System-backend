package com.drivinglicense.repository;

import com.drivinglicense.entity.ExamTypeConfig;
import com.drivinglicense.enums.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamTypeConfigRepository extends JpaRepository<ExamTypeConfig, Long> {

    Optional<ExamTypeConfig> findByExamType(ExamType examType);
}