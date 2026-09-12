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
public class AgentRecentRequestDTO {
    private Long id;
    private String requestNumber;
    private String applicantFullName;
    private ServiceType serviceType;
    private LocalDate date;
    private RequestStatus requestStatus;
}