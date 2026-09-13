package com.drivinglicense.repository;

import com.drivinglicense.entity.Request;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RequestRepository extends JpaRepository<Request, Long> {
    long countByCreationDate(LocalDate date);

    boolean existsByPersonIdAndServiceTypeAndRequestStatus(
            Long personId, ServiceType serviceType, RequestStatus requestStatus);

    @Query("SELECT r FROM Request r WHERE " +
            "(:status IS NULL OR r.requestStatus = :status) AND " +
            "(:serviceType IS NULL OR r.serviceType = :serviceType) AND " +
            "(:nationalNumber IS NULL OR r.person.nationalNumber = :nationalNumber)")
    Page<Request> filter(@Param("status") RequestStatus status,
                         @Param("serviceType") ServiceType serviceType,
                         @Param("nationalNumber") String nationalNumber,
                         Pageable pageable);

}