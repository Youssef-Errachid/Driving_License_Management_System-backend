package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.license.LicenseCreateDTO;
import com.drivinglicense.dto.license.LicenseResponseDTO;
import com.drivinglicense.entity.Driver;
import com.drivinglicense.entity.Exam;
import com.drivinglicense.entity.License;
import com.drivinglicense.entity.LicenseCategory;
import com.drivinglicense.entity.Person;
import com.drivinglicense.entity.PracticalExam;
import com.drivinglicense.entity.Request;
import com.drivinglicense.entity.TheoryExam;
import com.drivinglicense.entity.User;
import com.drivinglicense.entity.VisionExam;
import com.drivinglicense.enums.*;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.LicenseMapper;
import com.drivinglicense.repository.DriverRepository;
import com.drivinglicense.repository.ExamRepository;
import com.drivinglicense.repository.LicenseRepository;
import com.drivinglicense.repository.PaymentRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final RequestRepository requestRepository;
    private final DriverRepository driverRepository;
    private final ExamRepository examRepository;
    private final PaymentRepository paymentRepository;
    private final LicenseMapper licenseMapper;

    @Override
    @Transactional
    public LicenseResponseDTO create(LicenseCreateDTO dto) {
        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request", dto.getRequestId()));

        if (request.getRequestStatus() != RequestStatus.NEW) {
            throw new BusinessException("only a request with status NEW can result in a license issuance");
        }

        IssueReason issueReason = mapServiceTypeToIssueReason(request.getServiceType());

        requireApplicationFeePaid(request);

        Person person = request.getPerson();
        LicenseCategory category = request.getLicenseCategory();
        if (category == null) {
            throw new BusinessException("the request has no associated license category");
        }

        Driver driver;
        if (issueReason == IssueReason.NEW) {
            validateThreeExamsPassed(request.getId());
            requireServiceFeePaid(request);

            driver = person.getDriver();
            if (driver == null) {
                driver = Driver.builder()
                        .driverNumber(generateDriverNumber())
                        .person(person)
                        .build();
                driver = driverRepository.save(driver);
            }
        } else {
            driver = person.getDriver();
            if (driver == null) {
                throw new BusinessException(
                        "this person does not have a driver profile yet; a first license must be issued via NEW_LICENSE first");
            }

            if (issueReason == IssueReason.RENEWAL) {
                validateVisionExamPassed(request.getId());
            } else {
                requireServiceFeePaid(request);
            }
        }

        License license = licenseMapper.toEntity(dto);
        license.setLicenseNumber(generateLicenseNumber());
        license.setExpirationDate(LocalDate.now().plusYears(category.getValidationDurationYears()));
        license.setHolderNationalNumber(person.getNationalNumber());
        license.setHolderFullName(person.getFirstName() + " " + person.getLastName());
        license.setHolderBirthDate(person.getBirthDay());
        license.setIssueReason(issueReason);
        license.setDriver(driver);
        license.setLicenseCategory(category);
        license.setIssuingAgent(getCurrentUser());

        License saved = licenseRepository.save(license);

        request.setRequestStatus(RequestStatus.COMPLETE);

        return licenseMapper.toResponseDTO(saved);
    }

    @Override
    public LicenseResponseDTO getById(Long id) {
        return licenseMapper.toResponseDTO(findLicenseOrThrow(id));
    }

    @Override
    @Cacheable(value = "licenses", key = "#id")
    public List<LicenseResponseDTO> getByDriverId(Long driverId) {
        return licenseMapper.toResponseDTOList(licenseRepository.findByDriver_Id(driverId));
    }

    private IssueReason mapServiceTypeToIssueReason(ServiceType serviceType) {
        return switch (serviceType) {
            case NEW_LICENSE -> IssueReason.NEW;
            case RENEWAL -> IssueReason.RENEWAL;
            case LOST_DUPLICATE -> IssueReason.REPLACEMENT_LOST;
            case DAMAGED_DUPLICATE -> IssueReason.REPLACEMENT_DAMAGED;
            default -> throw new BusinessException(
                    "service type " + serviceType + " does not result in a license issuance");
        };
    }

    private void requireApplicationFeePaid(Request request) {
        if (!paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.APPLICATION_FEE)) {
            throw new BusinessException("the application fee for this request has not been paid yet");
        }
    }

    private void requireServiceFeePaid(Request request) {
        if (!paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.SERVICE)) {
            throw new BusinessException("the service fee for this request has not been paid yet");
        }
    }

    private void validateThreeExamsPassed(Long requestId) {
        List<Exam> exams = examRepository.findByRequest_IdOrderByIdAsc(requestId);
        boolean visionPassed = exams.stream()
                .anyMatch(e -> e instanceof VisionExam && e.getResult() == ExamResult.PASSED);
        boolean theoryPassed = exams.stream()
                .anyMatch(e -> e instanceof TheoryExam && e.getResult() == ExamResult.PASSED);
        boolean practicalPassed = exams.stream()
                .anyMatch(e -> e instanceof PracticalExam && e.getResult() == ExamResult.PASSED);

        if (!visionPassed || !theoryPassed || !practicalPassed) {
            throw new BusinessException(
                    "all three exams (vision, theory, practical) must be passed before issuing the license");
        }
    }

    private void validateVisionExamPassed(Long requestId) {
        List<Exam> exams = examRepository.findByRequest_IdOrderByIdAsc(requestId);
        boolean visionPassed = exams.stream()
                .anyMatch(e -> e instanceof VisionExam && e.getResult() == ExamResult.PASSED);
        if (!visionPassed) {
            throw new BusinessException("the vision exam must be passed before renewing the license");
        }
    }

    private String generateLicenseNumber() {
        return "LIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateDriverNumber() {
        return "DRV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private License findLicenseOrThrow(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("License", id));
    }

    @Override
    public PageResponseDTO<LicenseResponseDTO> getAll(BlockingStatus blockingStatus, IssueReason issueReason,
                                                      String query, int page, int size) {
                Page<License> result = licenseRepository.filter(
                                blockingStatus, issueReason, query, PageRequest.of(page, size));
                List<LicenseResponseDTO> content = licenseMapper.toResponseDTOList(result.getContent());
                return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
            }
}