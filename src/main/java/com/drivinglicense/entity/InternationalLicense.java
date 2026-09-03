package com.drivinglicense.entity;

import com.drivinglicense.enums.InternationalPermitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class
InternationalLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "issue date is required")
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDate issueDate = LocalDate.now();

    @NotNull(message = "expiration date is required")
    @Column(nullable = false,updatable = false)
    private LocalDate expirationDate;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InternationalPermitStatus internationalPermitStatus = InternationalPermitStatus.ACTIVE;

    @NotNull(message = "international license should be related to an existing license with category 3")
    @ManyToOne(optional = false)
    @JoinColumn(name = "license_id", nullable = false,updatable = false)
    private License license;

    @NotNull(message = "international license should be related to an existing driver")
    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id",nullable = false,updatable = false)
    private Driver driver;
}
