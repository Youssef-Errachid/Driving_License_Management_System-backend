package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.payment.PaymentResponseDTO;
import com.drivinglicense.dto.payment.PaymentCreateDTO;
import com.drivinglicense.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> create(@Valid @RequestBody PaymentCreateDTO dto) {
        PaymentResponseDTO response = paymentService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment recorded successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> getById(@PathVariable Long id) {
        PaymentResponseDTO response = paymentService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponseDTO<List<PaymentResponseDTO>>> getByRequestId(@PathVariable Long requestId) {
        List<PaymentResponseDTO> response = paymentService.getByRequestId(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payments retrieved successfully.", response));
    }
}