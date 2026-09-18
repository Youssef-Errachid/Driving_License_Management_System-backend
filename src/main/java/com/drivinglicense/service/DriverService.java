package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.driver.DriverResponseDTO;

public interface DriverService {

    PageResponseDTO<DriverResponseDTO> getAll(int page, int size);

    DriverResponseDTO getById(Long id);

    DriverResponseDTO getByNationalNumber(String nationalNumber);

    DriverResponseDTO getByLicenseNumber(String licenseNumber);
}