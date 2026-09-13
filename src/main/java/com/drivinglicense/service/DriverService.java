package com.drivinglicense.service;

import com.drivinglicense.dto.driver.DriverResponseDTO;

public interface DriverService {

    DriverResponseDTO getById(Long id);

    DriverResponseDTO getByNationalNumber(String nationalNumber);

    DriverResponseDTO getByLicenseNumber(String licenseNumber);
}