package com.mcm.backend.service.admin;

import java.util.Map;

public interface FeedbackManagementService {
    Map<String, Object> getFeedbackList(Integer page, Integer size, Boolean resolved, String query);
    Map<String, String> markAsResolved(Integer id);
    Map<String, String> deleteFeedback(Integer id);
} 