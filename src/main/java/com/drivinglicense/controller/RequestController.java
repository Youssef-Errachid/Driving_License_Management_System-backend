package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.request.RequestCreateDTO;
import com.drivinglicense.dto.request.RequestResponseDTO;
import com.drivinglicense.enums.RequestStatus;
import com.drivinglicense.enums.ServiceType;
import com.drivinglicense.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<RequestResponseDTO>> create(@Valid @RequestBody RequestCreateDTO dto) {
        RequestResponseDTO response = requestService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Request created successfully.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RequestResponseDTO>> getById(@PathVariable Long id) {
        RequestResponseDTO response = requestService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Request retrieved successfully.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<RequestResponseDTO>>> getAll(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) ServiceType serviceType,
            @RequestParam(required = false) String nationalNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<RequestResponseDTO> response =
                requestService.getAll(status, serviceType, nationalNumber, page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Requests retrieved successfully.", response));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDTO<RequestResponseDTO>> cancel(@PathVariable Long id) {
        RequestResponseDTO response = requestService.cancel(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Request cancelled successfully.", response));
    }
}