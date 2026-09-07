package com.drivinglicense.dto.licensecategory;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCategoryCreateDTO {

    @NotBlank(message = "the name is required")
    private String name;
    private String description;

    @NotNull(message = "the minimum age is required")
    @Min(value = 1,message = "the minimum should be positive")
    private int minimumAge;

    @NotNull(message = "the validity duration  is required")
    @Min(value = 1,message = "the validity duration must be at least 1 year")
    private int validationDurationYears;

    @NotNull(message = "fee is required")
    @DecimalMin(value = "0.0",message = "fee should be positive")
    private BigDecimal fee;
}