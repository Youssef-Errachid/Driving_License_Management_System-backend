package com.drivinglicense.dto.driver;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDTO {
    private Long id;
    private String driverNumber;
    private LocalDate creationDate;
    private Long personId;
    private String personFullName;

}