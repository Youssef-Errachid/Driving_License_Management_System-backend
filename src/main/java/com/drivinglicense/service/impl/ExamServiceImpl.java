package com.drivinglicense.service.impl;

import com.drivinglicense.dto.exam.ExamResponseDTO;
import com.drivinglicense.dto.exam.ExamResultDTO;
import com.drivinglicense.dto.exam.ExamScheduleDTO;
import com.drivinglicense.entity.Exam;
import com.drivinglicense.entity.Payment;
import com.drivinglicense.entity.PracticalExam;
import com.drivinglicense.entity.Request;
import com.drivinglicense.entity.TheoryExam;
import com.drivinglicense.entity.VisionExam;
import com.drivinglicense.enums.ExamResult;
import com.drivinglicense.enums.ExamType;
import com.drivinglicense.enums.PaymentType;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.ExamMapper;
import com.drivinglicense.repository.ExamRepository;
import com.drivinglicense.repository.PaymentRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.drivinglicense.dto.common.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final RequestRepository requestRepository;
    private final PaymentRepository paymentRepository;
    private final ExamMapper examMapper;

    @Override
    @Transactional
    public ExamResponseDTO schedule(ExamScheduleDTO dto) {
        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request", dto.getRequestId()));

        List<Exam> existingExams = examRepository.findByRequest_IdOrderByIdAsc(request.getId());

        validateExamOrder(dto.getExamType(), existingExams);

        boolean hasPendingSameType = existingExams.stream()
                .anyMatch(e -> sameExamType(e, dto.getExamType()) && e.getResult() == null);
        if (hasPendingSameType) {
            throw new BusinessException("an appointment for this exam type is already pending for this request");
        }

        if (!paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.APPLICATION_FEE)) {
            throw new BusinessException("the application fee for this request has not been paid yet");
        }

        PaymentType examPaymentType = toPaymentType(dto.getExamType());
        Payment payment = paymentRepository
                .findFirstByRequest_IdAndPaymentTypeAndExamIsNull(request.getId(), examPaymentType)
                .orElseThrow(() -> new BusinessException("the fee for this exam has not been paid yet"));

        Exam exam = examMapper.toEntity(dto);
        exam.setRequest(request);
        Exam saved = examRepository.save(exam);

        payment.setExam(saved);

        return examMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ExamResponseDTO recordResult(Long id, ExamResultDTO dto) {
        Exam exam = findExamOrThrow(id);

        if (exam.getResult() != null) {
            throw new BusinessException("the result for this exam has already been recorded");
        }

        examMapper.updateResultFromDTO(dto, exam);
        return examMapper.toResponseDTO(exam);
    }

    @Override
    public ExamResponseDTO getById(Long id) {
        return examMapper.toResponseDTO(findExamOrThrow(id));
    }

    @Override
    public List<ExamResponseDTO> getByRequestId(Long requestId) {
        return examMapper.toResponseDTOList(examRepository.findByRequest_IdOrderByIdAsc(requestId));
    }

    @Override
    public PageResponseDTO<ExamResponseDTO> getAll(LocalDate appointmentDate, ExamType examType,
                                                   boolean pendingOnly, int page, int size) {
        Class<? extends Exam> examClass = toExamClass(examType);

        Page<Exam> result = examRepository.filter(
                appointmentDate, examClass, pendingOnly, PageRequest.of(page, size));

        List<ExamResponseDTO> content = examMapper.toResponseDTOList(result.getContent());
        return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    private Class<? extends Exam> toExamClass(ExamType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case VISION -> VisionExam.class;
            case THEORY -> TheoryExam.class;
            case PRACTICAL -> PracticalExam.class;
        };
    }

    private void validateExamOrder(ExamType requestedType, List<Exam> existingExams) {
        switch (requestedType) {
            case VISION -> {
            }
            case THEORY -> {
                if (!hasPassed(existingExams, ExamType.VISION)) {
                    throw new BusinessException("the vision exam must be passed before scheduling the theory exam");
                }
            }
            case PRACTICAL -> {
                if (!hasPassed(existingExams, ExamType.VISION) || !hasPassed(existingExams, ExamType.THEORY)) {
                    throw new BusinessException(
                            "the vision and theory exams must be passed before scheduling the practical exam");
                }
            }
        }
    }

    private boolean hasPassed(List<Exam> exams, ExamType type) {
        return exams.stream()
                .anyMatch(e -> sameExamType(e, type) && e.getResult() == ExamResult.PASSED);
    }

    private boolean sameExamType(Exam exam, ExamType type) {
        return switch (type) {
            case VISION -> exam instanceof VisionExam;
            case THEORY -> exam instanceof TheoryExam;
            case PRACTICAL -> exam instanceof PracticalExam;
        };
    }

    private PaymentType toPaymentType(ExamType type) {
        return switch (type) {
            case VISION -> PaymentType.VISION_EXAM;
            case THEORY -> PaymentType.THEORY_EXAM;
            case PRACTICAL -> PaymentType.PRACTICAL_EXAM;
        };
    }

    private Exam findExamOrThrow(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", id));
    }
}