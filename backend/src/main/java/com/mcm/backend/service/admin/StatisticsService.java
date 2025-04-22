package com.mcm.backend.service.admin;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getUserStatistics();
    Map<String, Object> getExhibitStatistics();
    Map<String, Object> getFeedbackStatistics();
    Map<String, Object> getSurveyStatistics();
} 