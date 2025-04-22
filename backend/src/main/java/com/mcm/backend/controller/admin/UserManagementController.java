package com.mcm.backend.controller.admin;

import com.mcm.backend.service.admin.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/admin/users/")
    public Map<String, Object> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status
    ) {
        return userManagementService.getUserList(page, size, query, role, status);
    }

    @PostMapping("/admin/users/{userId}/ban/")
    public Map<String, String> toggleBan(
            @PathVariable Integer userId,
            @RequestBody Map<String, Boolean> request
    ) {
        return userManagementService.toggleBan(userId, request.get("ban"));
    }

    @PostMapping("/admin/users/{userId}/admin/")
    public Map<String, String> toggleAdmin(
            @PathVariable Integer userId,
            @RequestBody Map<String, Boolean> request
    ) {
        return userManagementService.toggleAdmin(userId, request.get("isAdmin"));
    }
} 