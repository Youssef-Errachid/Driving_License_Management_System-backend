package com.drivinglicense.dto.licensecategory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCategoryUpdateDTO {
    private String description;

    @Min(value = 1,message = "the minimum should be positive")
    private Integer minimumAge;

    @Min(value = 1,message = "the validity duration must be at least 1 year")
    private Integer validationDurationYears;

    @DecimalMin(value = "0.0",message = "fee should be positive")
    private BigDecimal fee;
}