package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.driver.DriverResponseDTO;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<DriverResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<DriverResponseDTO> response = driverService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Drivers retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DriverResponseDTO>> getById(@PathVariable Long id) {
        DriverResponseDTO response = driverService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Driver retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<DriverResponseDTO>> search(
            @RequestParam(required = false) String nationalNumber,
            @RequestParam(required = false) String licenseNumber) {
        DriverResponseDTO response;
        if (nationalNumber != null && !nationalNumber.isBlank()) {
            response = driverService.getByNationalNumber(nationalNumber);
        } else if (licenseNumber != null && !licenseNumber.isBlank()) {
            response = driverService.getByLicenseNumber(licenseNumber);
        } else {
            throw new BusinessException("either nationalNumber or licenseNumber must be provided");
        }
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Driver retrieved successfully.", response));
    }
}