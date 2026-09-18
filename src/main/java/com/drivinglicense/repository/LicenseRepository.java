package com.drivinglicense.repository;

import com.drivinglicense.entity.License;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.IssueReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    long countByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    long countByBlockingStatus(BlockingStatus blockingStatus);

    List<License> findByDriver_Id(Long driverId);

    Optional<License> findByLicenseNumber(String licenseNumber);

    @Query("SELECT l FROM License l WHERE (:blockingStatus IS NULL OR l.blockingStatus = :blockingStatus) AND (:issueReason IS NULL OR l.issueReason = :issueReason) AND (:query IS NULL OR LOWER(l.licenseNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(l.holderNationalNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(l.holderFullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<License> filter(@Param("blockingStatus") BlockingStatus blockingStatus,
                             @Param("issueReason") IssueReason issueReason,
                             @Param("query") String query,
                             Pageable pageable);

}