package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.request.RequestCreateDTO;
import com.drivinglicense.dto.request.RequestResponseDTO;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;

public interface RequestService {

    RequestResponseDTO create(RequestCreateDTO dto);

    RequestResponseDTO getById(Long id);

    PageResponseDTO<RequestResponseDTO> getAll(RequestStatus status,
                                               ServiceType serviceType,
                                               String nationalNumber,
                                               int page,
                                               int size);

    RequestResponseDTO cancel(Long id);
}