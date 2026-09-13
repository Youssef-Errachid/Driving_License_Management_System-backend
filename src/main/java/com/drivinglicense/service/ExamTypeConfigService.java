package com.drivinglicense.service;

import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigResponseDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigUpdateDTO;

import java.util.List;

public interface ExamTypeConfigService {

    List<ExamTypeConfigResponseDTO> getAll();

    ExamTypeConfigResponseDTO getById(Long id);

    ExamTypeConfigResponseDTO update(Long id, ExamTypeConfigUpdateDTO dto);
}