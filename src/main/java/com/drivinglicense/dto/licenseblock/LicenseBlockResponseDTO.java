package com.drivinglicense.dto.licenseblock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LicenseBlockResponseDTO {
    private Long id;
    private String reason;
    private BigDecimal fineAmount;
    private LocalDate blockingDate;
    private  LocalDate unblockingDate;
    private Long licenseId;
    private String licenseNumber;
}