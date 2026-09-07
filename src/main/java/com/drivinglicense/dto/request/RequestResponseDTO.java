package com.drivinglicense.dto.request;

import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseDTO {
    private Long id;
    private LocalDate creationDate;
    private LocalDate cancellationDate;
    private RequestStatus requestStatus;
    private ServiceType serviceType;
    private Long personId;
    private String personFullName;
    private Long licenseCategoryId;
    private String cancelledByEmail;
    private Long originalRequestId;
}