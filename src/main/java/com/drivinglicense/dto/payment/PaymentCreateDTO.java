package com.drivinglicense.dto.payment;

import com.drivinglicense.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDTO {
    @NotNull(message = "request id is required")
    private Long requestId;
    @NotNull(message = "payment type is required")
    private PaymentType paymentType;
    private Long examId;
    private Long licenseBlockId;
}