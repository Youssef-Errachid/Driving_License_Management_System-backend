package com.drivinglicense.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LicenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "the name is required")
    @Column(unique = true,nullable = false,updatable = false)
    private String name;

    private String description;

    @NotNull(message = "the minimum age is required")
    @Min(value = 1 , message = "the minimum should be positive")
    @Column(nullable = false)
    private int minimumAge;

    @NotNull(message = "the duration is required")
    @Min(value = 1 , message = "the validity duration must be at least 1 year")
    @Column(nullable = false)
    private int validationDurationYears;

    @NotNull(message = "fee is required")
    @DecimalMin(value = "0.0", message = "fee should be positive no negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;
}
