package com.drivinglicense.dto.license;

import com.drivinglicense.enums.BlockingStatus;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseSummaryDTO {
    private Long id;
    private String licenseName;
    private LocalDate expirationDate;
    private BlockingStatus blockingStatus;
}