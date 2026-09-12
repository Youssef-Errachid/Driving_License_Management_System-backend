package com.drivinglicense.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponseDTO {
    private long requestsToday;
    private ExamsTodayDTO examsToday;
    private long licensesIssuedThisWeek;
    private long activeUsers;
    private long blockedLicenses;
    private List<AdminRecentRequestDTO> recentRequests;
}