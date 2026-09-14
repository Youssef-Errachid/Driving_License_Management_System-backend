package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.request.RequestCreateDTO;
import com.drivinglicense.dto.request.RequestResponseDTO;
import com.drivinglicense.entity.License;
import com.drivinglicense.entity.LicenseCategory;
import com.drivinglicense.entity.Person;
import com.drivinglicense.entity.Request;
import com.drivinglicense.entity.User;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.RequestMapper;
import com.drivinglicense.repository.LicenseCategoryRepository;
import com.drivinglicense.repository.LicenseRepository;
import com.drivinglicense.repository.PersonRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final PersonRepository personRepository;
    private final LicenseCategoryRepository licenseCategoryRepository;
    private final LicenseRepository licenseRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestResponseDTO create(RequestCreateDTO dto) {
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("Person", dto.getPersonId()));

        boolean hasOpenRequest = requestRepository.existsByPersonIdAndServiceTypeAndRequestStatus(
                person.getId(), dto.getServiceType(), RequestStatus.NEW);
        if (hasOpenRequest) {
            throw new BusinessException("an open request of the same type already exists for this person");
        }

        Request request = requestMapper.toEntity(dto);
        request.setPerson(person);

        if (dto.getLicenseCategoryId() != null) {
            LicenseCategory category = licenseCategoryRepository.findById(dto.getLicenseCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("LicenseCategory", dto.getLicenseCategoryId()));
            request.setLicenseCategory(category);

            if (dto.getServiceType() == ServiceType.NEW_LICENSE) {
                validateMinimumAge(person, category);
            }
        }

        if (dto.getLicenseId() != null) {
            License license = licenseRepository.findById(dto.getLicenseId())
                    .orElseThrow(() -> new ResourceNotFoundException("License", dto.getLicenseId()));

            if (dto.getServiceType() == ServiceType.UNBLOCKING
                    && license.getBlockingStatus() != BlockingStatus.BLOCKED) {
                throw new BusinessException("this license is not currently blocked");
            }

            request.setLicense(license);
        }

        if (dto.getOriginalRequestId() != null) {
            Request original = requestRepository.findById(dto.getOriginalRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Request", dto.getOriginalRequestId()));
            request.setOriginalRequest(original);
        }

        Request saved = requestRepository.save(request);
        return requestMapper.toResponseDTO(saved);
    }

    @Override
    public RequestResponseDTO getById(Long id) {
        return requestMapper.toResponseDTO(findRequestOrThrow(id));
    }

    @Override
    public PageResponseDTO<RequestResponseDTO> getAll(RequestStatus status, ServiceType serviceType,
                                                      String nationalNumber, int page, int size) {
        Page<Request> result = requestRepository.filter(
                status, serviceType, nationalNumber, PageRequest.of(page, size));
        List<RequestResponseDTO> content = requestMapper.toResponseDTOList(result.getContent());
        return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    @Override
    @Transactional
    public RequestResponseDTO cancel(Long id) {
        Request request = findRequestOrThrow(id);

        if (request.getRequestStatus() != RequestStatus.NEW) {
            throw new BusinessException("only a request with status NEW can be cancelled");
        }

        request.setRequestStatus(RequestStatus.CANCELLED);
        request.setCancellationDate(LocalDate.now());
        request.setCancelledBy(getCurrentUser());

        return requestMapper.toResponseDTO(request);
    }

    private void validateMinimumAge(Person person, LicenseCategory category) {
        int age = LocalDate.now().getYear() - person.getBirthDay().getYear();
        if (person.getBirthDay().plusYears(age).isAfter(LocalDate.now())) {
            age--;
        }
        if (age < category.getMinimumAge()) {
            throw new BusinessException("the person does not meet the minimum age required for this category");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private Request findRequestOrThrow(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request", id));
    }
}