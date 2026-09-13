package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryResponseDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryUpdateDTO;
import com.drivinglicense.service.LicenseCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/license-categories")
@RequiredArgsConstructor
public class LicenseCategoryController {

    private final LicenseCategoryService licenseCategoryService;

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LicenseCategoryResponseDTO>>> getAll() {
        List<LicenseCategoryResponseDTO> response = licenseCategoryService.getAll();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License categories retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LicenseCategoryResponseDTO>> getById(@PathVariable Long id) {
        LicenseCategoryResponseDTO response = licenseCategoryService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License category retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LicenseCategoryResponseDTO>> update(
            @PathVariable Long id, @Valid @RequestBody LicenseCategoryUpdateDTO dto) {
        LicenseCategoryResponseDTO response = licenseCategoryService.update(id, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "License category updated successfully.", response));
    }
}