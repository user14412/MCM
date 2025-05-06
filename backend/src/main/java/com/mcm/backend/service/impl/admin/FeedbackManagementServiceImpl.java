package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcm.backend.mapper.FeedbackMapper;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.Feedback;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.admin.FeedbackManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackManagementServiceImpl implements FeedbackManagementService {
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getFeedbackList(Integer page, Integer size, Boolean resolved, String query) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");
        
        // 先查询总数
        QueryWrapper<Feedback> countWrapper = new QueryWrapper<>();
        if (resolved != null) {
            countWrapper.eq("resolved", resolved);
        }
        
        // 添加搜索条件
        if (query != null && !query.trim().isEmpty()) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("username", query)
                          .or()
                          .like("id", query);
            
            List<User> matchedUsers = userMapper.selectList(userQueryWrapper);
            List<Integer> matchedUserIds = matchedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
            
            countWrapper.and(wrapper -> wrapper
                .like("id", query)
                .or()
                .in(matchedUserIds.size() > 0, "user_id", matchedUserIds)
            );
        }
        
        // 获取总数
        long total = feedbackMapper.selectCount(countWrapper);
        resp.put("total", total);
        
        // 分页查询
        IPage<Feedback> feedbackPage = new Page<>(page, size);
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        
        if (resolved != null) {
            queryWrapper.eq("resolved", resolved);
        }
        
        // 添加搜索条件
        if (query != null && !query.trim().isEmpty()) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("username", query)
                          .or()
                          .like("id", query);
            
            List<User> matchedUsers = userMapper.selectList(userQueryWrapper);
            List<Integer> matchedUserIds = matchedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());
            
            queryWrapper.and(wrapper -> wrapper
                .like("id", query)
                .or()
                .in(matchedUserIds.size() > 0, "user_id", matchedUserIds)
            );
        }
        
        queryWrapper.orderByDesc("createtime");
        IPage<Feedback> result = feedbackMapper.selectPage(feedbackPage, queryWrapper);
        
        // 获取所有反馈的用户ID
        List<Integer> userIds = result.getRecords().stream()
            .map(Feedback::getUserId)
            .collect(Collectors.toList());
        
        // 批量查询用户信息
        List<User> users = new ArrayList<>();
        if (!userIds.isEmpty()) {
            users = userMapper.selectBatchIds(userIds);
        }
        Map<Integer, User> userMap = users.stream()
            .collect(Collectors.toMap(User::getId, user -> user));
        
        // 将用户信息添加到反馈中
        List<Map<String, Object>> feedbackList = result.getRecords().stream()
            .map(feedback -> {
                Map<String, Object> feedbackMap = new HashMap<>();
                feedbackMap.put("id", feedback.getId());
                feedbackMap.put("userId", feedback.getUserId());
                feedbackMap.put("contactInfo", feedback.getContactInfo());
                feedbackMap.put("content", feedback.getContent());
                feedbackMap.put("createtime", feedback.getCreatetime());
                feedbackMap.put("resolved", feedback.getResolved());
                
                // 添加用户信息
                User user = userMap.get(feedback.getUserId());
                if (user != null) {
                    feedbackMap.put("username", user.getUsername());
                    feedbackMap.put("userPhoto", user.getPhoto());
                }
                
                return feedbackMap;
            })
            .collect(Collectors.toList());
        
        resp.put("items", feedbackList);
        
        return resp;
    }

    @Override
    public Map<String, String> markAsResolved(Integer id) {
        Map<String, String> resp = new HashMap<>();
        
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            resp.put("error_message", "反馈不存在");
            return resp;
        }
        
        feedback.setResolved(true);
        feedbackMapper.updateById(feedback);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> deleteFeedback(Integer id) {
        Map<String, String> resp = new HashMap<>();
        
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            resp.put("error_message", "反馈不存在");
            return resp;
        }
        
        feedbackMapper.deleteById(id);
        resp.put("error_message", "success");
        
        return resp;
    }
} 