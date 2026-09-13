package com.drivinglicense.service.impl;

import com.drivinglicense.dto.driver.DriverResponseDTO;
import com.drivinglicense.entity.Driver;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.DriverMapper;
import com.drivinglicense.repository.DriverRepository;
import com.drivinglicense.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverResponseDTO getById(Long id) {
        return driverMapper.toResponseDTO(findDriverOrThrow(id));
    }

    @Override
    public DriverResponseDTO getByNationalNumber(String nationalNumber) {
        Driver driver = driverRepository.findByPerson_NationalNumber(nationalNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found for national number: " + nationalNumber));
        return driverMapper.toResponseDTO(driver);
    }

    private Driver findDriverOrThrow(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", id));
    }
}