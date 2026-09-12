package com.drivinglicense.dto.dashboard;

import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecentRequestDTO {
    private Long id;
    private String nationalNumber;
    private String fullName;
    private ServiceType serviceType;
    private RequestStatus requestStatus;
    private LocalDate date;
}