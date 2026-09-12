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
public class AgentDashboardResponseDTO {
    private long requestsToday;
    private ExamsTodayDTO examsToday;
    private long licensesIssuedThisWeek;
    private List<AgentRecentRequestDTO> recentRequests;
}