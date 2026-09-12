package com.drivinglicense.repository;

import com.drivinglicense.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RequestRepository extends JpaRepository<Request, Long> {
    long countByCreationDate(LocalDate date);
}