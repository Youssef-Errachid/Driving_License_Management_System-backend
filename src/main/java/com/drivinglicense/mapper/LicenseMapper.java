package com.drivinglicense.mapper;

import com.drivinglicense.dto.license.LicenseCreateDTO;
import com.drivinglicense.dto.license.LicenseResponseDTO;
import com.drivinglicense.dto.license.LicenseSummaryDTO;
import com.drivinglicense.entity.License;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LicenseMapper {
    default License toEntity(LicenseCreateDTO dto){
        License license = new License();
        license.setConditions(dto.getConditions());
        license.setHolderPhoto(dto.getHolderPhoto());

        return license;
    }

    default LicenseResponseDTO toResponseDTO(License license){

        LicenseResponseDTO dto = new LicenseResponseDTO();
        dto.setId(license.getId());
        dto.setLicenseNumber(license.getLicenseNumber());
        dto.setHolderPhoto(license.getHolderPhoto());
        dto.setIssueDate(license.getIssueDate());
        dto.setExpirationDate(license.getExpirationDate());
        dto.setConditions(license.getConditions());
        dto.setHolderNationalNumber(license.getHolderNationalNumber());
        dto.setHolderFullName(license.getHolderFullName());
        dto.setHolderBirthDate(license.getHolderBirthDate());
        dto.setIssueReason(license.getIssueReason());
        dto.setBlockingStatus(license.getBlockingStatus());

        if(license.getDriver() != null){
            dto.setDriverId(license.getDriver().getId());
        }
        if(license.getLicenseCategory() != null){
            dto.setLicenseCategoryName(license.getLicenseCategory().getName());
        }
        if(license.getIssuingAgent()  != null){
            dto.setIssuingAgentEmail(license.getIssuingAgent().getEmail());
        }

        return dto;
    }

    default LicenseSummaryDTO toSummaryDTO(License license){
        LicenseSummaryDTO dto = new LicenseSummaryDTO();
        dto.setId(license.getId());
        dto.setLicenseName(license.getLicenseNumber());
        dto.setExpirationDate(license.getExpirationDate());
        dto.setBlockingStatus(license.getBlockingStatus());

        return dto;
    }

    List<LicenseResponseDTO> toResponseDTOList(List<License> licenses);
    List<LicenseSummaryDTO> toSummaryDTOList(List<License> licenses);
}