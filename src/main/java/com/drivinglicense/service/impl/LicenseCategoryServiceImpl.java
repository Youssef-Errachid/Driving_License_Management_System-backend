package com.drivinglicense.service.impl;

import com.drivinglicense.dto.licensecategory.LicenseCategoryResponseDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryUpdateDTO;
import com.drivinglicense.entity.LicenseCategory;
import com.drivinglicense.exception.ResourceNotFoundException;
import com.drivinglicense.mapper.LicenseCategoryMapper;
import com.drivinglicense.repository.LicenseCategoryRepository;
import com.drivinglicense.service.LicenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseCategoryServiceImpl implements LicenseCategoryService {

    private final LicenseCategoryRepository licenseCategoryRepository;
    private final LicenseCategoryMapper licenseCategoryMapper;

    @Override
    public List<LicenseCategoryResponseDTO> getAll() {
        return licenseCategoryMapper.toResponseDTOList(licenseCategoryRepository.findAll());
    }

    @Override
    public LicenseCategoryResponseDTO getById(Long id) {
        return licenseCategoryMapper.toResponseDTO(findCategoryOrThrow(id));
    }

    @Override
    @Transactional
    public LicenseCategoryResponseDTO update(Long id, LicenseCategoryUpdateDTO dto) {
        LicenseCategory category = findCategoryOrThrow(id);
        licenseCategoryMapper.updateEntityFromDTO(dto, category);
        return licenseCategoryMapper.toResponseDTO(category);
    }

    private LicenseCategory findCategoryOrThrow(Long id) {
        return licenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LicenseCategory", id));
    }
}