package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.license.LicenseCreateDTO;
import com.drivinglicense.dto.license.LicenseResponseDTO;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.IssueReason;

import java.util.List;

public interface LicenseService {

    LicenseResponseDTO create(LicenseCreateDTO dto);

    LicenseResponseDTO getById(Long id);

    List<LicenseResponseDTO> getByDriverId(Long driverId);

    PageResponseDTO<LicenseResponseDTO> getAll(BlockingStatus blockingStatus, IssueReason issueReason,
                                               String query, int page, int size);
}