package com.drivinglicense.entity;

import com.drivinglicense.enums.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "amount should be positive")
    @Column(nullable = false,updatable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Builder.Default
    @NotNull(message = "payment date is required")
    @Column(nullable = false,updatable = false)
    private LocalDate paymentDate = LocalDate.now();

    @NotNull(message = "payment type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @NotNull(message = "payment should be related to an existing request")
    @ManyToOne(optional = false)
    @JoinColumn(name = "request_id", nullable = false,updatable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "exam_id", updatable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "license_block_id", updatable = false)
    private LicenseBlock licenseBlock;
}
