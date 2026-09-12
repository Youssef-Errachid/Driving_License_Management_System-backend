package com.drivinglicense.service.impl;

import com.drivinglicense.dto.dashboard.*;
import com.drivinglicense.entity.Request;
import com.drivinglicense.enums.BlockingStatus;
import com.drivinglicense.enums.UserStatus;
import com.drivinglicense.mapper.DashboardMapper;
import com.drivinglicense.repository.ExamRepository;
import com.drivinglicense.repository.LicenseRepository;
import com.drivinglicense.repository.RequestRepository;
import com.drivinglicense.repository.UserRepository;
import com.drivinglicense.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_REQUESTS_LIMIT = 5;

    private final RequestRepository requestRepository;
    private final ExamRepository examRepository;
    private final LicenseRepository licenseRepository;
    private final UserRepository userRepository;
    private final DashboardMapper dashboardMapper;

    @Override
    public AdminDashboardResponseDTO getAdminDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        AdminDashboardResponseDTO dto = new AdminDashboardResponseDTO();
        dto.setRequestsToday(requestRepository.countByCreationDate(today));
        dto.setExamsToday(buildExamsToday(today));
        dto.setLicensesIssuedThisWeek(licenseRepository.countByIssueDateBetween(startOfWeek, today));
        dto.setActiveUsers(userRepository.countByUserStatus(UserStatus.ACTIVE));
        dto.setBlockedLicenses(licenseRepository.countByBlockingStatus(BlockingStatus.BLOCKED));
        dto.setRecentRequests(getAdminRecentRequests());
        return dto;
    }

    @Override
    public AgentDashboardResponseDTO getAgentDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        AgentDashboardResponseDTO dto = new AgentDashboardResponseDTO();
        dto.setRequestsToday(requestRepository.countByCreationDate(today));
        dto.setExamsToday(buildExamsToday(today));
        dto.setLicensesIssuedThisWeek(licenseRepository.countByIssueDateBetween(startOfWeek, today));
        dto.setRecentRequests(getAgentRecentRequests());
        return dto;
    }

    private ExamsTodayDTO buildExamsToday(LocalDate date) {
        long vision = examRepository.countVisionByAppointmentDate(date);
        long theory = examRepository.countTheoryByAppointmentDate(date);
        long practical = examRepository.countPracticalByAppointmentDate(date);
        return new ExamsTodayDTO(vision + theory + practical, vision, theory, practical);
    }

    private List<Request> findRecentRequests() {
        return requestRepository
                .findAll(PageRequest.of(0, RECENT_REQUESTS_LIMIT, Sort.by(Sort.Direction.DESC, "creationDate", "id")))
                .getContent();
    }

    private List<AdminRecentRequestDTO> getAdminRecentRequests() {
        return dashboardMapper.toAdminRecentRequestDTOList(findRecentRequests());
    }

    private List<AgentRecentRequestDTO> getAgentRecentRequests() {
        return dashboardMapper.toAgentRecentRequestDTOList(findRecentRequests());
    }
}