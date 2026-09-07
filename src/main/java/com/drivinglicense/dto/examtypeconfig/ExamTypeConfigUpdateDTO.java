package com.drivinglicense.dto.examtypeconfig;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamTypeConfigUpdateDTO {

    @NotNull(message = "fee is required")
    @DecimalMin(value = "0.0",message = "fee should be positive")
    private BigDecimal fee;
}