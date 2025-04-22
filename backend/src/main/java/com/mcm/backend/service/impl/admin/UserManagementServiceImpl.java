package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.admin.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getUserList(Integer page, Integer size, String query, String role, String status) {
        // 创建分页对象，设置当前页和每页大小
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 搜索条件
        if (query != null && !query.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("id", query)
                .or()
                .like("username", query)
            );
        }

        // 角色筛选
        if (role != null && !role.isEmpty()) {
            if ("admin".equals(role)) {
                queryWrapper.eq("is_admin", true);
            } else if ("user".equals(role)) {
                queryWrapper.eq("is_admin", false);
            }
        }

        // 状态筛选
        if (status != null && !status.isEmpty()) {
            if ("active".equals(status)) {
                queryWrapper.eq("is_banned", false);
            } else if ("banned".equals(status)) {
                queryWrapper.eq("is_banned", true);
            }
        }

        // 执行分页查询
        IPage<User> result = userMapper.selectPage(userPage, queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("items", result.getRecords());
        map.put("total", result.getTotal());
        return map;
    }

    @Override
    public Map<String, String> toggleBan(Integer userId, Boolean ban) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            Map<String, String> map = new HashMap<>();
            map.put("error_message", "用户不存在");
            return map;
        }

        user.setIsBanned(ban);
        userMapper.updateById(user);

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> toggleAdmin(Integer userId, Boolean isAdmin) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            Map<String, String> map = new HashMap<>();
            map.put("error_message", "用户不存在");
            return map;
        }

        user.setIsAdmin(isAdmin);
        userMapper.updateById(user);

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        return map;
    }
} 