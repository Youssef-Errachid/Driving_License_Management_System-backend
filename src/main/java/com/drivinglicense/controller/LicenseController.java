package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.license.LicenseCreateDTO;
import com.drivinglicense.dto.license.LicenseResponseDTO;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.IssueReason;
import com.drivinglicense.service.LicenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LicenseResponseDTO>> create(@Valid @RequestBody LicenseCreateDTO dto) {
        LicenseResponseDTO response = licenseService.create(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License issued successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LicenseResponseDTO>> getById(@PathVariable Long id) {
        LicenseResponseDTO response = licenseService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponseDTO<List<LicenseResponseDTO>>> getByDriverId(@PathVariable Long driverId) {
        List<LicenseResponseDTO> response = licenseService.getByDriverId(driverId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Licenses retrieved successfully.", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<LicenseResponseDTO>>> getAll(
            @RequestParam(required = false) BlockingStatus blockingStatus,
            @RequestParam(required = false) IssueReason issueReason,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                PageResponseDTO<LicenseResponseDTO> response =
                                licenseService.getAll(blockingStatus, issueReason, query, page, size);
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Licenses retrieved successfully.", response));
            }
}