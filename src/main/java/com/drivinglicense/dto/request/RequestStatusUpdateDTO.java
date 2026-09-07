package com.drivinglicense.dto.request;

import com.drivinglicense.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusUpdateDTO {
    @NotNull(message = "request status is required")
    private RequestStatus requestStatus;
}