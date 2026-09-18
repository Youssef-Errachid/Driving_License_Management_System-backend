package com.drivinglicense.service.impl;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.driver.DriverResponseDTO;
import com.drivinglicense.entity.Driver;
import com.drivinglicense.entity.License;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.DriverMapper;
import com.drivinglicense.repository.DriverRepository;
import com.drivinglicense.repository.LicenseRepository;
import com.drivinglicense.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final LicenseRepository licenseRepository;
    private final DriverMapper driverMapper;

    @Override
    public PageResponseDTO<DriverResponseDTO> getAll(int page, int size) {
        Page<Driver> result = driverRepository.findAll(PageRequest.of(page, size));
        List<DriverResponseDTO> content = driverMapper.toResponseDTOList(result.getContent());
        return new PageResponseDTO<>(content, result.getNumber(), result.getTotalPages(), result.getTotalElements());
    }

    @Override
    @Cacheable(value = "drivers", key = "'id_' + #id")
    public DriverResponseDTO getById(Long id) {
        return driverMapper.toResponseDTO(findDriverOrThrow(id));
    }

    @Override
    @Cacheable(value = "drivers", key = "'nn_' + #nationalNumber")
    public DriverResponseDTO getByNationalNumber(String nationalNumber) {
        Driver driver = driverRepository.findByPerson_NationalNumber(nationalNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found for national number: " + nationalNumber));
        return driverMapper.toResponseDTO(driver);
    }

    @Override
    @Cacheable(value = "drivers", key = "'ln_' + #licenseNumber")
    public DriverResponseDTO getByLicenseNumber(String licenseNumber) {
        License license = licenseRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "License not found for license number: " + licenseNumber));
        return driverMapper.toResponseDTO(license.getDriver());
    }

    private Driver findDriverOrThrow(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", id));
    }
}