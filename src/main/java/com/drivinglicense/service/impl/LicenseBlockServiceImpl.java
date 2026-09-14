package com.drivinglicense.service.impl;

import com.drivinglicense.dto.licenseblock.LicenseBlockCreateDTO;
import com.drivinglicense.dto.licenseblock.LicenseBlockResponseDTO;
import com.drivinglicense.entity.License;
import com.drivinglicense.entity.LicenseBlock;
import com.drivinglicense.entity.Request;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.PaymentType;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.LicenseBlockMapper;
import com.drivinglicense.repository.LicenseBlockRepository;
import com.drivinglicense.repository.LicenseRepository;
import com.drivinglicense.repository.PaymentRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.service.LicenseBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseBlockServiceImpl implements LicenseBlockService {

    private final LicenseBlockRepository licenseBlockRepository;
    private final LicenseRepository licenseRepository;
    private final RequestRepository requestRepository;
    private final PaymentRepository paymentRepository;
    private final LicenseBlockMapper licenseBlockMapper;

    @Override
    @Transactional
    public LicenseBlockResponseDTO block(LicenseBlockCreateDTO dto) {
        License license = licenseRepository.findById(dto.getLicenseId())
                .orElseThrow(() -> new ResourceNotFoundException("License", dto.getLicenseId()));

        if (license.getBlockingStatus() == BlockingStatus.BLOCKED) {
            throw new BusinessException("this license is already blocked");
        }

        LicenseBlock licenseBlock = licenseBlockMapper.toEntity(dto);
        licenseBlock.setLicense(license);

        LicenseBlock saved = licenseBlockRepository.save(licenseBlock);
        license.setBlockingStatus(BlockingStatus.BLOCKED);

        return licenseBlockMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public LicenseBlockResponseDTO unblock(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request", requestId));

        if (request.getRequestStatus() != RequestStatus.NEW) {
            throw new BusinessException("only a request with status NEW can result in an unblocking");
        }
        if (request.getServiceType() != ServiceType.UNBLOCKING) {
            throw new BusinessException("this request is not an unblocking request");
        }
        License license = request.getLicense();
        if (license == null) {
            throw new BusinessException("this request has no associated license");
        }

        LicenseBlock activeBlock = licenseBlockRepository.findByLicense_IdAndUnblockingDateIsNull(license.getId())
                .orElseThrow(() -> new BusinessException("no active block found for this license"));

        if (!paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.APPLICATION_FEE)) {
            throw new BusinessException("the application fee for this request has not been paid yet");
        }
        if (!paymentRepository.existsByRequest_IdAndPaymentType(request.getId(), PaymentType.FINE)) {
            throw new BusinessException("the fine for this license has not been paid yet");
        }

        activeBlock.setUnblockingDate(LocalDate.now());
        license.setBlockingStatus(BlockingStatus.UNBLOCKED);
        request.setRequestStatus(RequestStatus.COMPLETE);

        return licenseBlockMapper.toResponseDTO(activeBlock);
    }

    @Override
    public List<LicenseBlockResponseDTO> getByLicenseId(Long licenseId) {
        return licenseBlockMapper.toResponseDTOList(licenseBlockRepository.findByLicense_Id(licenseId));
    }
}