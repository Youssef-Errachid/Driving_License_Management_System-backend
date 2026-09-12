package com.drivinglicense.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamsTodayDTO {
    private long total;
    private long vision;
    private long theory;
    private long practical;
}