package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.driver.DriverResponseDTO;
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DriverResponseDTO>> getById(@PathVariable Long id) {
        DriverResponseDTO response = driverService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Driver retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<DriverResponseDTO>> searchByNationalNumber(
            @RequestParam String nationalNumber) {
        DriverResponseDTO response = driverService.getByNationalNumber(nationalNumber);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Driver retrieved successfully.", response));
    }
}