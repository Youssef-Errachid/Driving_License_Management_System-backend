package com.drivinglicense.mapper;

import com.drivinglicense.dto.request.RequestCreateDTO;
import com.drivinglicense.dto.request.RequestResponseDTO;
import com.drivinglicense.dto.request.RequestStatusUpdateDTO;
import com.drivinglicense.entity.Request;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    default Request toEntity(RequestCreateDTO dto){
        Request request = new Request();
        request.setServiceType(dto.getServiceType());
        return request;
    }

    default RequestResponseDTO toResponseDTO(Request request){
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setId(request.getId());
        dto.setCreationDate(request.getCreationDate());
        dto.setCancellationDate(request.getCancellationDate());
        dto.setRequestStatus(request.getRequestStatus());
        dto.setServiceType(request.getServiceType());

        if(request.getPerson() != null){
            dto.setPersonId(request.getPerson().getId());
            dto.setPersonFullName(request.getPerson().getFirstName() + " " + request.getPerson().getLastName());
        }
        if(request.getLicenseCategory() != null){
            dto.setLicenseCategoryId(request.getLicenseCategory().getId());
        }
        if(request.getCancelledBy() != null){
            dto.setCancelledByEmail(request.getCancelledBy().getEmail());
        }
        if(request.getOriginalRequest() != null){
            dto.setOriginalRequestId(request.getOriginalRequest().getId());
        }

        return dto;
    }

    default void updateStatusFromDTO(RequestStatusUpdateDTO dto,Request request){
        request.setRequestStatus(dto.getRequestStatus());
    }

    List<RequestResponseDTO> toResponseDTOList(List<Request> requests);
}