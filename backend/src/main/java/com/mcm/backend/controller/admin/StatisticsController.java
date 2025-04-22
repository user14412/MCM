package com.mcm.backend.controller.admin;

import com.mcm.backend.service.admin.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/statistics/")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("user/")
    public Map<String, Object> getUserStatistics() {
        return statisticsService.getUserStatistics();
    }

    @GetMapping("exhibit/")
    public Map<String, Object> getExhibitStatistics() {
        return statisticsService.getExhibitStatistics();
    }

    @GetMapping("feedback/")
    public Map<String, Object> getFeedbackStatistics() {
        return statisticsService.getFeedbackStatistics();
    }

    @GetMapping("survey/")
    public Map<String, Object> getSurveyStatistics() {
        return statisticsService.getSurveyStatistics();
    }
} 