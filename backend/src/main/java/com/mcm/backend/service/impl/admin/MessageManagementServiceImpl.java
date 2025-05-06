package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcm.backend.mapper.MessageMapper;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.Message;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.admin.MessageManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageManagementServiceImpl implements MessageManagementService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getMessageList(Integer page, Integer size, String status, String query) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");

        QueryWrapper<Message> countWrapper = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            countWrapper.eq("status", status);
        }

        // 搜索条件
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
                    .like("content", query)
                    .or()
                    .in(matchedUserIds.size() > 0, "user_id", matchedUserIds)
            );
        }

        long total = messageMapper.selectCount(countWrapper);
        resp.put("total", total);

        IPage<Message> messagePage = new Page<>(page, size);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
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
                    .like("content", query)
                    .or()
                    .in(matchedUserIds.size() > 0, "user_id", matchedUserIds)
            );
        }
        queryWrapper.orderByDesc("createtime");
        IPage<Message> result = messageMapper.selectPage(messagePage, queryWrapper);

        // 获取所有留言的用户ID
        List<Integer> userIds = result.getRecords().stream()
                .map(Message::getUserId)
                .collect(Collectors.toList());

        // 批量查询用户信息
        List<User> users = new ArrayList<>();
        if (!userIds.isEmpty()) {
            users = userMapper.selectBatchIds(userIds);
        }
        Map<Integer, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 组装返回数据
        List<Map<String, Object>> messageList = result.getRecords().stream()
                .map(message -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", message.getId());
                    map.put("userId", message.getUserId());
                    map.put("scene", message.getScene());
                    map.put("content", message.getContent());
                    map.put("createtime", message.getCreatetime());
                    map.put("status", message.getStatus());
                    map.put("username", message.getUsername());
                    map.put("userphoto", message.getUserphoto());
                    User user = userMap.get(message.getUserId());
                    if (user != null) {
                        map.put("username", user.getUsername());
                        map.put("userphoto", user.getPhoto());
                    }
                    return map;
                })
                .collect(Collectors.toList());

        resp.put("items", messageList);
        return resp;
    }

    @Override
    public Map<String, String> acceptMessage(Integer id) {
        Map<String, String> resp = new HashMap<>();
        Message message = messageMapper.selectById(id);
        if (message == null) {
            resp.put("error_message", "留言不存在");
            return resp;
        }
        message.setStatus("accepted");
        messageMapper.updateById(message);
        resp.put("error_message", "success");
        return resp;
    }

    @Override
    public Map<String, String> rejectMessage(Integer id) {
        Map<String, String> resp = new HashMap<>();
        Message message = messageMapper.selectById(id);
        if (message == null) {
            resp.put("error_message", "留言不存在");
            return resp;
        }
        message.setStatus("rejected");
        messageMapper.updateById(message);
        resp.put("error_message", "success");
        return resp;
    }

    @Override
    public Map<String, String> deleteMessage(Integer id) {
        Map<String, String> resp = new HashMap<>();
        Message message = messageMapper.selectById(id);
        if (message == null) {
            resp.put("error_message", "留言不存在");
            return resp;
        }
        messageMapper.deleteById(id);
        resp.put("error_message", "success");
        return resp;
    }
} 