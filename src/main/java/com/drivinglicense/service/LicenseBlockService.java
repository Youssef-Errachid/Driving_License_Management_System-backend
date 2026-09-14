package com.drivinglicense.service;

import com.drivinglicense.dto.licenseblock.LicenseBlockCreateDTO;
import com.drivinglicense.dto.licenseblock.LicenseBlockResponseDTO;

import java.util.List;

public interface LicenseBlockService {

    LicenseBlockResponseDTO block(LicenseBlockCreateDTO dto);

    LicenseBlockResponseDTO unblock(Long requestId);

    List<LicenseBlockResponseDTO> getByLicenseId(Long licenseId);
}