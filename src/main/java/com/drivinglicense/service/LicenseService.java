package com.drivinglicense.service;

import com.drivinglicense.dto.license.LicenseCreateDTO;
import com.drivinglicense.dto.license.LicenseResponseDTO;

import java.util.List;

public interface LicenseService {

    LicenseResponseDTO create(LicenseCreateDTO dto);

    LicenseResponseDTO getById(Long id);

    List<LicenseResponseDTO> getByDriverId(Long driverId);
}