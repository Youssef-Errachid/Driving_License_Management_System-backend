package com.drivinglicense.repository;

import com.drivinglicense.entity.License;
import com.drivinglicense.enums.BlockingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    long countByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    long countByBlockingStatus(BlockingStatus blockingStatus);

    List<License> findByDriver_Id(Long driverId);

    Optional<License> findByLicenseNumber(String licenseNumber);
}