package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigResponseDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigUpdateDTO;
import com.drivinglicense.service.ExamTypeConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-type-configs")
@RequiredArgsConstructor
public class ExamTypeConfigController {

    private final ExamTypeConfigService examTypeConfigService;

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ExamTypeConfigResponseDTO>>> getAll() {
        List<ExamTypeConfigResponseDTO> response = examTypeConfigService.getAll();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam type configs retrieved successfully.", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ExamTypeConfigResponseDTO>> getById(@PathVariable Long id) {
        ExamTypeConfigResponseDTO response = examTypeConfigService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam type config retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ExamTypeConfigResponseDTO>> update(
            @PathVariable Long id, @Valid @RequestBody ExamTypeConfigUpdateDTO dto) {
        ExamTypeConfigResponseDTO response = examTypeConfigService.update(id, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam type config updated successfully.", response));
    }
}