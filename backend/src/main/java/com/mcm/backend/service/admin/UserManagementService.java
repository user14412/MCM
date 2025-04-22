package com.mcm.backend.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mcm.backend.pojo.User;
import java.util.Map;

public interface UserManagementService {
    Map<String, Object> getUserList(Integer page, Integer size, String query, String role, String status);
    Map<String, String> toggleBan(Integer userId, Boolean ban);
    Map<String, String> toggleAdmin(Integer userId, Boolean isAdmin);
} 