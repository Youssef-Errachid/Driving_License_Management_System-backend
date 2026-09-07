package com.drivinglicense.mapper;

import com.drivinglicense.dto.licensecategory.LicenseCategoryCreateDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryResponseDTO;
import com.drivinglicense.dto.licensecategory.LicenseCategoryUpdateDTO;
import com.drivinglicense.entity.LicenseCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LicenseCategoryMapper {
    LicenseCategory toEntity(LicenseCategoryCreateDTO dto);

    LicenseCategoryResponseDTO toResponseDTO(LicenseCategory licenseCategory);

    List<LicenseCategoryResponseDTO> toResponseDTOList(List<LicenseCategory> licenseCategories);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(LicenseCategoryUpdateDTO dto, @MappingTarget LicenseCategory licenseCategory);

}