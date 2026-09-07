package com.drivinglicense.dto.request;

import com.drivinglicense.enums.ServiceType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateDTO {
    @NotNull(message = "person id is required")
    private Long personId;
    @NotNull(message = "service type is required")
    private ServiceType serviceType;

    private Long licenseCategoryId;
}