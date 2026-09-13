package com.drivinglicense.service;

import com.drivinglicense.dto.exam.ExamResponseDTO;
import com.drivinglicense.dto.exam.ExamResultDTO;
import com.drivinglicense.dto.exam.ExamScheduleDTO;

import java.util.List;

public interface ExamService {

    ExamResponseDTO schedule(ExamScheduleDTO dto);

    ExamResponseDTO recordResult(Long id, ExamResultDTO dto);

    ExamResponseDTO getById(Long id);

    List<ExamResponseDTO> getByRequestId(Long requestId);
}