package com.drivinglicense.dto.exam;
import com.drivinglicense.enums.ExamResult;
import com.drivinglicense.enums.ExamType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDTO {
    private Long id;
    private ExamType examType;
    private LocalDate appointmentDate;
    private LocalDate resultDate;
    private ExamResult examResult;
    private Integer score;
    private  Long requestId;

}