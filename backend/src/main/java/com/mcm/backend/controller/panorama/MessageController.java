package com.mcm.backend.controller.panorama;

import com.mcm.backend.service.panorama.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/panorama/message/add/")
    public Map<String, String> addMessage(@RequestParam Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        try {
            String content = data.get("content");
            String scene = data.get("scene");
            
            if (content == null || content.trim().isEmpty()) {
                response.put("error_message", "留言内容不能为空");
                return response;
            }
            
            if (scene == null || scene.trim().isEmpty()) {
                response.put("error_message", "场景不能为空");
                return response;
            }
            
            return messageService.addMessage(content, scene);
        } catch (Exception e) {
            response.put("error_message", "服务器内部错误");
            return response;
        }
    }

    @GetMapping("/panorama/message/get/")
    public Map<String, Object> getMessages(@RequestParam Map<String, String> data) {
//        System.out.println("开始获取留言");
        Map<String, Object> response = new HashMap<>();
        try {
            String scene = data.get("scene");
            
            if (scene == null || scene.trim().isEmpty()) {
                response.put("error_message", "场景不能为空");
                return response;
            }
            
            return messageService.getMessages(scene);
        } catch (Exception e) {
            response.put("error_message", "服务器内部错误");
            return response;
        }
    }
} 