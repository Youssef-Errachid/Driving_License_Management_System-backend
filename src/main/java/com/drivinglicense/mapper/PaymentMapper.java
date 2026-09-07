package com.drivinglicense.mapper;

import com.drivinglicense.dto.payment.PaymentCreateDTO;
import com.drivinglicense.dto.payment.PaymentResponseDTO;
import com.drivinglicense.entity.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    default Payment toEntity(PaymentCreateDTO dto){
        Payment payment = new Payment();
        payment.setPaymentType(dto.getPaymentType());
        return payment;
    }

    default PaymentResponseDTO toResponseDTO(Payment payment){
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentType(payment.getPaymentType());

        if(payment.getRequest() != null){
            dto.setRequestId(payment.getRequest().getId());
        }
        if(payment.getExam() != null){
            dto.setExamId(payment.getExam().getId());
        }
        if (payment.getLicenseBlock() != null){
            dto.setLicenseBlockId(payment.getLicenseBlock().getId());
        }

        return dto;
    }

    List<PaymentResponseDTO> toResponseDTOList(List<Payment> payments);
}