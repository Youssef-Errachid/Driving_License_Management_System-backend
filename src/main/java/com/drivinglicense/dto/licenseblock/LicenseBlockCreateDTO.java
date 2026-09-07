package com.drivinglicense.dto.licenseblock;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LicenseBlockCreateDTO {

    @NotBlank(message = "reason is required")
    private String reason;

    @NotNull(message = "fine amount is required")
    @DecimalMin(value = "0.0", message = "fine amount should be positive")
    private BigDecimal fineAmount;

    @NotNull(message = "license id is required")
    private Long licenseId;
}