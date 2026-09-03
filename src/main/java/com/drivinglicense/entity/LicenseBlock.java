package com.drivinglicense.entity;

import com.drivinglicense.enums.BlockingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class LicenseBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "reason is required")
    @Column(nullable = false,updatable = false)
    private String reason;

    @NotNull(message = "fine amount is required")
    @DecimalMin(value = "0.0", message = "fine amount should be positive")
    @Column(nullable = false,updatable = false)
    private BigDecimal fineAmount;

    @NotNull(message = "blocking date is required")
    @Column(nullable = false,updatable = false)
    @Builder.Default
    private LocalDate blockingDate = LocalDate.now();

    private LocalDate unblockingDate;

    @NotNull(message = "blocking should be related to license")
    @ManyToOne(optional = false)
    @JoinColumn(name = "license_id", nullable = false, updatable = false)
    private License license;
}
