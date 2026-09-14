package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.licenseblock.LicenseBlockCreateDTO;
import com.drivinglicense.dto.licenseblock.LicenseBlockResponseDTO;
import com.drivinglicense.service.LicenseBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/license-blocks")
@RequiredArgsConstructor
public class LicenseBlockController {

    private final LicenseBlockService licenseBlockService;

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LicenseBlockResponseDTO>> block(
            @Valid @RequestBody LicenseBlockCreateDTO dto) {
        LicenseBlockResponseDTO response = licenseBlockService.block(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License blocked successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @PatchMapping("/unblock/{requestId}")
    public ResponseEntity<ApiResponseDTO<LicenseBlockResponseDTO>> unblock(@PathVariable Long requestId) {
        LicenseBlockResponseDTO response = licenseBlockService.unblock(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License unblocked successfully.", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping("/license/{licenseId}")
    public ResponseEntity<ApiResponseDTO<List<LicenseBlockResponseDTO>>> getByLicenseId(
            @PathVariable Long licenseId) {
        List<LicenseBlockResponseDTO> response = licenseBlockService.getByLicenseId(licenseId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License blocks retrieved successfully.", response));
    }
}