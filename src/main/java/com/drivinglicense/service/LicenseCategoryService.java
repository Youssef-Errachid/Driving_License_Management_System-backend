package com.drivinglicense.service;

import com.drivinglicense.dto.licensecategory.LicenseCategoryResponseDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryUpdateDTO;

import java.util.List;

public interface LicenseCategoryService {

    List<LicenseCategoryResponseDTO> getAll();

    LicenseCategoryResponseDTO getById(Long id);

    LicenseCategoryResponseDTO update(Long id, LicenseCategoryUpdateDTO dto);
}