package com.drivinglicense.service;

import com.drivinglicense.dto.dashboard.AdminDashboardResponseDTO;
import com.drivinglicense.dto.dashboard.AgentDashboardResponseDTO;

public interface DashboardService {
    AdminDashboardResponseDTO getAdminDashboard();
    AgentDashboardResponseDTO getAgentDashboard();
}