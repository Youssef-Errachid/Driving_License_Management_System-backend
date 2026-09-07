package com.drivinglicense.dto.license;

import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.IssueReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseResponseDTO {
    private Long id;
    private String licenseNumber;
    private String holderPhoto;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private String conditions;
    private String holderNationalNumber;
    private String holderFullName;
    private LocalDate holderBirthDate;
    private IssueReason issueReason;
    private Long driverId;
    private String licenseCategoryName;
    private String issuingAgentEmail;
    private BlockingStatus blockingStatus;
}