package com.drivinglicense.service.impl;

import com.drivinglicense.dto.payment.PaymentCreateDTO;
import com.drivinglicense.dto.payment.PaymentResponseDTO;
import com.drivinglicense.entity.Exam;
import com.drivinglicense.entity.ExamTypeConfig;
import com.drivinglicense.entity.LicenseBlock;
import com.drivinglicense.entity.LicenseCategory;
import com.drivinglicense.entity.Payment;
import com.drivinglicense.entity.Request;
import com.drivinglicense.enums.ExamType;
import com.drivinglicense.enums.PaymentType;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.PaymentMapper;
import com.drivinglicense.repository.ExamRepository;
import com.drivinglicense.repository.ExamTypeConfigRepository;
import com.drivinglicense.repository.LicenseBlockRepository;
import com.drivinglicense.repository.PaymentRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RequestRepository requestRepository;
    private final ExamRepository examRepository;
    private final ExamTypeConfigRepository examTypeConfigRepository;
    private final LicenseBlockRepository licenseBlockRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDTO create(PaymentCreateDTO dto) {
        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request", dto.getRequestId()));

        if (request.getRequestStatus() != RequestStatus.NEW) {
            throw new BusinessException("payments can only be recorded for a request with status NEW");
        }

        BigDecimal amount = computeAmount(dto, request);

        Payment payment = paymentMapper.toEntity(dto);
        payment.setAmount(amount);
        payment.setRequest(request);

        if (dto.getExamId() != null) {
            Exam exam = examRepository.findById(dto.getExamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exam", dto.getExamId()));
            if (!exam.getRequest().getId().equals(request.getId())) {
                throw new BusinessException("the exam does not belong to this request");
            }
            payment.setExam(exam);
        }

        if (dto.getLicenseBlockId() != null) {
            LicenseBlock block = licenseBlockRepository.findById(dto.getLicenseBlockId())
                    .orElseThrow(() -> new ResourceNotFoundException("LicenseBlock", dto.getLicenseBlockId()));
            payment.setLicenseBlock(block);
        }

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toResponseDTO(saved);
    }

    @Override
    public PaymentResponseDTO getById(Long id) {
        return paymentMapper.toResponseDTO(
                paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment", id)));
    }

    @Override
    public List<PaymentResponseDTO> getByRequestId(Long requestId) {
        return paymentMapper.toResponseDTOList(paymentRepository.findByRequest_Id(requestId));
    }

    private BigDecimal computeAmount(PaymentCreateDTO dto, Request request) {
        return switch (dto.getPaymentType()) {
            case APPLICATION_FEE -> computeApplicationFee(request);
            case VISION_EXAM -> examTypeFee(ExamType.VISION);
            case THEORY_EXAM -> examTypeFee(ExamType.THEORY);
            case PRACTICAL_EXAM -> practicalExamFee(request);
            case SERVICE -> serviceFee(request);
            case FINE -> fineFee(dto);
        };
    }

    private BigDecimal computeApplicationFee(Request request) {
        if (paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.APPLICATION_FEE)) {
            throw new BusinessException("the application fee has already been paid for this request");
        }
        return request.getServiceType() == ServiceType.RENEWAL ? BigDecimal.valueOf(10) : BigDecimal.valueOf(5);
    }

    private BigDecimal examTypeFee(ExamType type) {
        ExamTypeConfig config = examTypeConfigRepository.findByExamType(type)
                .orElseThrow(() -> new BusinessException("no fee configured for exam type " + type));
        return config.getFee();
    }

    private BigDecimal practicalExamFee(Request request) {
        LicenseCategory category = request.getLicenseCategory();
        if (category == null) {
            throw new BusinessException("this request has no associated license category");
        }
        return category.getFee();
    }

    private BigDecimal serviceFee(Request request) {
        return switch (request.getServiceType()) {
            case NEW_LICENSE -> {
                LicenseCategory category = request.getLicenseCategory();
                if (category == null) {
                    throw new BusinessException("this request has no associated license category");
                }
                yield category.getFee();
            }
            case LOST_DUPLICATE, DAMAGED_DUPLICATE, INTERNATIONAL_LICENSE -> BigDecimal.valueOf(20);
            default -> throw new BusinessException(
                    "service type " + request.getServiceType() + " does not have a SERVICE fee component");
        };
    }

    private BigDecimal fineFee(PaymentCreateDTO dto) {
        if (dto.getLicenseBlockId() == null) {
            throw new BusinessException("licenseBlockId is required for a FINE payment");
        }
        LicenseBlock block = licenseBlockRepository.findById(dto.getLicenseBlockId())
                .orElseThrow(() -> new ResourceNotFoundException("LicenseBlock", dto.getLicenseBlockId()));
        return block.getFineAmount();
    }
}