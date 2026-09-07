package com.drivinglicense.dto.examtypeconfig;

import com.drivinglicense.enums.ExamType;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamTypeConfigResponseDTO {
    private Long id;
    private ExamType examType;
    private BigDecimal fee;
}