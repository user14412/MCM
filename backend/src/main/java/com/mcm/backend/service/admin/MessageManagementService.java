package com.mcm.backend.service.admin;

import java.util.Map;

public interface MessageManagementService {
    Map<String, Object> getMessageList(Integer page, Integer size, String status, String query);
    Map<String, String> acceptMessage(Integer id);
    Map<String, String> rejectMessage(Integer id);
    Map<String, String> deleteMessage(Integer id);
} 