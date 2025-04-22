package com.mcm.backend.controller.admin;

import com.mcm.backend.service.admin.FeedbackManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/feedback/")
public class FeedbackManagementController {
    @Autowired
    private FeedbackManagementService feedbackManagementService;

    @GetMapping("list/")
    public Map<String, Object> getFeedbackList(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Boolean resolved,
                                             @RequestParam(required = false) String query) {
        return feedbackManagementService.getFeedbackList(page, size, resolved, query);
    }

    @PutMapping("{id}/resolve/")
    public Map<String, String> markAsResolved(@PathVariable Integer id) {
        return feedbackManagementService.markAsResolved(id);
    }

    @DeleteMapping("{id}/")
    public Map<String, String> deleteFeedback(@PathVariable Integer id) {
        return feedbackManagementService.deleteFeedback(id);
    }
} 