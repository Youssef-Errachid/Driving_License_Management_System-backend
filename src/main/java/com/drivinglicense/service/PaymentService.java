package com.drivinglicense.service;

import com.drivinglicense.dto.payment.PaymentCreateDTO;
import com.drivinglicense.dto.payment.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    PaymentResponseDTO create(PaymentCreateDTO dto);
    PaymentResponseDTO getById(Long id);
    List<PaymentResponseDTO> getByRequestId(Long requestId);
}