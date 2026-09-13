package com.drivinglicense.dto.license;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseCreateDTO {

    @NotNull(message = "request id is required")
    private Long requestId;

    private String holderPhoto;

    private String conditions;
}