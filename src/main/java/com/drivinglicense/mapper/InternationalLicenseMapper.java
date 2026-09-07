package com.drivinglicense.mapper;

import com.drivinglicense.dto.internationallicense.InternationalLicenseCreateDTO;
import com.drivinglicense.dto.internationallicense.InternationalLicenseResponseDTO;
import com.drivinglicense.entity.InternationalLicense;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InternationalLicenseMapper {
    default InternationalLicense toEntity(InternationalLicenseCreateDTO dto){
        return new InternationalLicense();
    }

    default InternationalLicenseResponseDTO toResponseDTO(InternationalLicense internationalLicense){
        InternationalLicenseResponseDTO dto = new InternationalLicenseResponseDTO();
        dto.setId(internationalLicense.getId());
        dto.setIssueDate(internationalLicense.getIssueDate());
        dto.setInternationalPermitStatus(internationalLicense.getInternationalPermitStatus());

        if(internationalLicense.getLicense() != null){
            dto.setLicenseId(internationalLicense.getLicense().getId());
            dto.setLicenseNumber(internationalLicense.getLicense().getLicenseNumber());
        }
        if(internationalLicense.getDriver() != null){
            dto.setDriverId(internationalLicense.getDriver().getId());
        }

        return dto;
    }

    List<InternationalLicenseResponseDTO> toResponseDTOList(List<InternationalLicense> internationalLicenses);
}