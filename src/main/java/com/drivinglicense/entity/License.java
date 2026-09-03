package com.drivinglicense.entity;

import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.IssueReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "license number is required")
    @Column(unique = true, nullable = false,updatable = false)
    private String licenseNumber;

    private String holderPhoto;

    @NotNull
    @Builder.Default
    @Column(nullable = false,updatable = false)
    private LocalDate issueDate = LocalDate.now();

    @NotNull(message = "expiration date is required")
    @Column(nullable = false)
    private LocalDate expirationDate;

    private String conditions;

    @NotBlank(message = "golder national number is required")
    @Column(nullable = false,updatable = false)
    private String holderNationalNumber;

    @NotBlank(message = "holder full name is required")
    @Column(nullable = false,updatable = false)
    private String holderFullName;

    @NotNull
    @Past(message = "holder birth date should be in the past")
    @Column(nullable = false,updatable = false)
    private LocalDate holderBirthDate;

    @NotNull(message = "issue reason is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,updatable = false)
    private IssueReason issueReason;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlockingStatus blockingStatus = BlockingStatus.UNBLOCKED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie_id", nullable = false, updatable = false)
    private LicenseCategory licenseCategory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "issuing_agent_id", nullable = false, updatable = false)
    private User issuingAgent ;
}
