package com.drivinglicense.controller;

import com.drivinglicense.dto.common.ApiResponseDTO;
import com.drivinglicense.dto.dashboard.AdminDashboardResponseDTO;
import com.drivinglicense.dto.dashboard.AgentDashboardResponseDTO;
import com.drivinglicense.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<AdminDashboardResponseDTO>> getAdminDashboard() {
        AdminDashboardResponseDTO response = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Admin dashboard fetched successfully.", response));
    }

    @GetMapping("/api/agent/dashboard")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponseDTO<AgentDashboardResponseDTO>> getAgentDashboard() {
        AgentDashboardResponseDTO response = dashboardService.getAgentDashboard();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Agent dashboard fetched successfully.", response));
    }
}