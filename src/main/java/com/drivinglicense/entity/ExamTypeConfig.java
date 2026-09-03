package com.drivinglicense.entity;

import com.drivinglicense.enums.ExamType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExamTypeConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "the type of exam is required")
    @Column(nullable = false,unique = true,updatable = false)
    @Enumerated(EnumType.STRING)
    private ExamType examType;

    @NotNull(message = "fee is required")
    @DecimalMin(value = "0.0", message = "fee should be positive no negative")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;
}
