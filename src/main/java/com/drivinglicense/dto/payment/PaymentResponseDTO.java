package com.drivinglicense.dto.payment;

import com.drivinglicense.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentType paymentType;
    private Long requestId;
    private Long examId;
    private Long licenseBlockId;
}