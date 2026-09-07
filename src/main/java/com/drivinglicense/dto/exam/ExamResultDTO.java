package com.drivinglicense.dto.exam;

import com.drivinglicense.enums.ExamResult;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultDTO {
    @NotNull(message = "result is required")
    private ExamResult result;

    @Min(value = 0, message = "score should be positive")
    @Max(value = 40, message = "score can't exceed 40")
    private  Integer score;
}