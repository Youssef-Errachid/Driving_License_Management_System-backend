package com.drivinglicense.dto.license;

import com.drivinglicense.enums.IssueReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseCreateDTO {
    @NotNull(message = "driver id is required")
    private Long driverId;
    private String holderPhoto;
    private String conditions;

    @NotNull(message = "issue reason is required")
    private IssueReason issueReason;

    @NotNull(message = "license category id is required")
    private Long licenseCategoryId;
}