package com.drivinglicense.mapper;

import com.drivinglicense.dto.licenseblock.LicenseBlockCreateDTO;
import com.drivinglicense.dto.licenseblock.LicenseBlockResponseDTO;
import com.drivinglicense.entity.LicenseBlock;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LicenseBlockMapper {
    default LicenseBlock toEntity(LicenseBlockCreateDTO dto){
        LicenseBlock licenseBlock = new LicenseBlock();
        licenseBlock.setReason(dto.getReason());
        licenseBlock.setFineAmount(dto.getFineAmount());
        return licenseBlock;
    }

    default LicenseBlockResponseDTO toResponseDTO(LicenseBlock licenseBlock){
        LicenseBlockResponseDTO dto = new LicenseBlockResponseDTO();
        dto.setId(licenseBlock.getId());
        dto.setReason(licenseBlock.getReason());
        dto.setFineAmount(licenseBlock.getFineAmount());
        dto.setBlockingDate(licenseBlock.getBlockingDate());
        dto.setUnblockingDate(licenseBlock.getUnblockingDate());

        if(licenseBlock.getLicense() != null){
            dto.setLicenseId(licenseBlock.getLicense().getId());
            dto.setLicenseNumber(licenseBlock.getLicense().getLicenseNumber());
        }
        return dto;
    }

    List<LicenseBlockResponseDTO> toResponseDTOList(List<LicenseBlock> licenseBlocks);
}