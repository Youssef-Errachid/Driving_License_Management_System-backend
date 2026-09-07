package com.drivinglicense.dto.examtypeconfig;

import com.drivinglicense.enums.ExamType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamTypeConfigCreateDTO {
    @NotNull(message = "the type of the exam is required")
    private ExamType examType;

    @DecimalMin(value = "0.0",message = "fee should be positive")
    @NotNull(message = "fee is required")
    private BigDecimal fee;
}