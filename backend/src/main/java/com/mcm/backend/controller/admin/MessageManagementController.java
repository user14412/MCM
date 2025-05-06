package com.mcm.backend.controller.admin;

import com.mcm.backend.service.admin.MessageManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/message/")
public class MessageManagementController {
    @Autowired
    private MessageManagementService messageManagementService;

    @GetMapping("list/")
    public Map<String, Object> getMessageList(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(required = false) String status,
                                              @RequestParam(required = false) String query) {
        return messageManagementService.getMessageList(page, size, status, query);
    }

    @PutMapping("{id}/accept/")
    public Map<String, String> acceptMessage(@PathVariable Integer id) {
        return messageManagementService.acceptMessage(id);
    }

    @PutMapping("{id}/reject/")
    public Map<String, String> rejectMessage(@PathVariable Integer id) {
        return messageManagementService.rejectMessage(id);
    }

    @DeleteMapping("{id}/")
    public Map<String, String> deleteMessage(@PathVariable Integer id) {
        return messageManagementService.deleteMessage(id);
    }
} 