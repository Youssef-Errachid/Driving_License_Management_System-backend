package com.drivinglicense.mapper;

import com.drivinglicense.dto.dashboard.AdminRecentRequestDTO;
import com.drivinglicense.dto.dashboard.AgentRecentRequestDTO;
import com.drivinglicense.entity.Request;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    default AdminRecentRequestDTO toAdminRecentRequestDTO(Request request) {
        AdminRecentRequestDTO dto = new AdminRecentRequestDTO();
        dto.setId(request.getId());
        if (request.getPerson() != null) {
            dto.setNationalNumber(request.getPerson().getNationalNumber());
            dto.setFullName(request.getPerson().getFirstName() + " " + request.getPerson().getLastName());
        }
        dto.setServiceType(request.getServiceType());
        dto.setRequestStatus(request.getRequestStatus());
        dto.setDate(request.getCreationDate());
        return dto;
    }

    List<AdminRecentRequestDTO> toAdminRecentRequestDTOList(List<Request> requests);

    default AgentRecentRequestDTO toAgentRecentRequestDTO(Request request) {
        AgentRecentRequestDTO dto = new AgentRecentRequestDTO();
        dto.setId(request.getId());
        // Pas de champ "requestNumber" en base -> genere depuis l'id.
        dto.setRequestNumber("REQ-" + request.getId());
        if (request.getPerson() != null) {
            dto.setApplicantFullName(request.getPerson().getFirstName() + " " + request.getPerson().getLastName());
        }
        dto.setServiceType(request.getServiceType());
        dto.setDate(request.getCreationDate());
        dto.setRequestStatus(request.getRequestStatus());
        return dto;
    }

    List<AgentRecentRequestDTO> toAgentRecentRequestDTOList(List<Request> requests);
}