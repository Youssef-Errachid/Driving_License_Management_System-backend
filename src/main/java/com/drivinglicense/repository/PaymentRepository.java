package com.drivinglicense.repository;

import com.drivinglicense.entity.Payment;
import com.drivinglicense.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByRequest_Id(Long requestId);

    boolean existsByRequest_Id(Long requestId);

    boolean existsByRequest_IdAndPaymentType(Long requestId, PaymentType paymentType);

    Optional<Payment> findFirstByRequest_IdAndPaymentTypeAndExamIsNull(Long requestId, PaymentType paymentType);

    @Query("select distinct p.request.id from Payment p where p.request.id in :requestIds")
    List<Long> findRequestIdsWithPayments(@Param("requestIds") List<Long> requestIds);
}