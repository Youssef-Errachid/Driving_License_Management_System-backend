package com.drivinglicense.mapper;

import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigCreateDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigResponseDTO;
import com.drivinglicense.dto.examtypeconfig.ExamTypeConfigUpdateDTO;
import com.drivinglicense.entity.ExamTypeConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamTypeConfigMapper {
    ExamTypeConfig toEntity(ExamTypeConfigCreateDTO dto);

    ExamTypeConfigResponseDTO toResponseDTO(ExamTypeConfig examTypeConfig);

    List<ExamTypeConfigResponseDTO> toResponseDTOList(List<ExamTypeConfig> examTypeConfigs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ExamTypeConfigUpdateDTO dto, @MappingTarget ExamTypeConfig examTypeConfig);

}