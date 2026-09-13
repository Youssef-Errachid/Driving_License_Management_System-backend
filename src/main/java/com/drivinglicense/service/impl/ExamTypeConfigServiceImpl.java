package com.drivinglicense.service.impl;

import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigResponseDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigUpdateDTO;
import com.drivinglicense.entity.ExamTypeConfig;
import com.drivinglicense.enums.ExamType;
import com.drivinglicense.exception.BusinessException;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.ExamTypeConfigMapper;
import com.drivinglicense.repository.ExamTypeConfigRepository;
import com.drivinglicense.service.ExamTypeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamTypeConfigServiceImpl implements ExamTypeConfigService {

    private final ExamTypeConfigRepository examTypeConfigRepository;
    private final ExamTypeConfigMapper examTypeConfigMapper;

    @Override
    public List<ExamTypeConfigResponseDTO> getAll() {
        return examTypeConfigMapper.toResponseDTOList(examTypeConfigRepository.findAll());
    }

    @Override
    public ExamTypeConfigResponseDTO getById(Long id) {
        return examTypeConfigMapper.toResponseDTO(findConfigOrThrow(id));
    }

    @Override
    @Transactional
    public ExamTypeConfigResponseDTO update(Long id, ExamTypeConfigUpdateDTO dto) {
        ExamTypeConfig config = findConfigOrThrow(id);

        if (config.getExamType() == ExamType.PRACTICAL) {
            throw new BusinessException(
                    "the practical exam fee is defined per license category and cannot be updated here");
        }

        examTypeConfigMapper.updateEntityFromDTO(dto, config);
        return examTypeConfigMapper.toResponseDTO(config);
    }

    private ExamTypeConfig findConfigOrThrow(Long id) {
        return examTypeConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExamTypeConfig", id));
    }
}