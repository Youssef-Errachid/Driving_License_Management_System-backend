package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.exam.ExamResponseDTO;
import com.drivinglicense.dto.exam.ExamResultDTO;
import com.drivinglicense.dto.exam.ExamScheduleDTO;
import com.drivinglicense.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.enums.ExamType;
import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PreAuthorize("hasRole('AGENT')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ExamResponseDTO>> schedule(@Valid @RequestBody ExamScheduleDTO dto) {
        ExamResponseDTO response = examService.schedule(dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam scheduled successfully.", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ExamResponseDTO>>> getAll(
            @RequestParam(required = false) LocalDate appointmentDate,
            @RequestParam(required = false) ExamType examType,
            @RequestParam(defaultValue = "false") boolean pendingOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<ExamResponseDTO> response =
                examService.getAll(appointmentDate, examType, pendingOnly, page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exams retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @PatchMapping("/{id}/result")
    public ResponseEntity<ApiResponseDTO<ExamResponseDTO>> recordResult(
            @PathVariable Long id, @Valid @RequestBody ExamResultDTO dto) {
        ExamResponseDTO response = examService.recordResult(id, dto);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam result recorded successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ExamResponseDTO>> getById(@PathVariable Long id) {
        ExamResponseDTO response = examService.getById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exam retrieved successfully.", response));
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponseDTO<List<ExamResponseDTO>>> getByRequestId(@PathVariable Long requestId) {
        List<ExamResponseDTO> response = examService.getByRequestId(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Exams retrieved successfully.", response));
    }
}