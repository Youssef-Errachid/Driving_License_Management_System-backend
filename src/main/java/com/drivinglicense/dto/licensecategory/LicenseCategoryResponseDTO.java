package com.drivinglicense.dto.licensecategory;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private int minimumAge;
    private int validationDurationYears;
    private BigDecimal fee;
}