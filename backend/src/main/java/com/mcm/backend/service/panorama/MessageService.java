package com.mcm.backend.service.panorama;

import java.util.Map;

public interface MessageService {
    Map<String, String> addMessage(String content, String scene);
    Map<String, Object> getMessages(String scene);
} 