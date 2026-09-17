package com.drivinglicense.service;

import com.drivinglicense.dto.common.PageResponseDTO;
import com.drivinglicense.dto.exam.ExamResponseDTO;
import com.drivinglicense.dto.exam.ExamResultDTO;
import com.drivinglicense.dto.exam.ExamScheduleDTO;
import com.drivinglicense.enums.ExamType;

import java.time.LocalDate;
import java.util.List;

public interface ExamService {

    ExamResponseDTO schedule(ExamScheduleDTO dto);

    ExamResponseDTO recordResult(Long id, ExamResultDTO dto);

    ExamResponseDTO getById(Long id);

    List<ExamResponseDTO> getByRequestId(Long requestId);

    PageResponseDTO<ExamResponseDTO> getAll(LocalDate appointmentDate, ExamType examType,
                                            boolean pendingOnly, int page, int size);
}