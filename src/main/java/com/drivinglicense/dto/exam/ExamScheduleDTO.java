package com.drivinglicense.dto.exam;

import com.drivinglicense.enums.ExamType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamScheduleDTO {
    @NotNull(message = "request id is required")
    private Long requestId;

    @NotNull(message = "exam type is required")
    private ExamType examType;

    @NotNull(message = "appointment date is required")
    private LocalDate appointmentDate;
}