package com.mcm.backend.service.impl.panorama;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.MessageMapper;
import com.mcm.backend.pojo.Message;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.panorama.MessageService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Map<String, String> addMessage(String content, String scene) {
        Map<String, String> response = new HashMap<>();
        
        // 获取当前登录用户
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        // 创建新留言
        Message message = new Message();
        message.setContent(content);
        message.setUserId(user.getId());
        message.setUsername(user.getUsername());
        message.setUserphoto(user.getPhoto());
        message.setScene(scene);
        message.setStatus("pending"); // 默认状态为待审核

        try {
            messageMapper.insert(message);
            response.put("error_message", "success");
        } catch (Exception e) {
            response.put("error_message", "留言发布失败");
        }

        return response;
    }

    @Override
    public Map<String, Object> getMessages(String scene) {
        Map<String, Object> response = new HashMap<>();
        
        try {
//            System.out.println("进入获取留言服务");
//            System.out.println("查询场景: " + scene);
            
            // 查询已审核通过的留言
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("scene", scene)
//                    .eq("status", "accepted")
//                    .orderByDesc("createtime");
            // 不限场景
            queryWrapper.eq("status", "accepted")
                    .orderByDesc("createtime");

            
//            System.out.println("执行查询...");
            List<Message> messages = messageMapper.selectList(queryWrapper);
//            System.out.println("查询结果数量: " + (messages != null ? messages.size() : 0));
            
            // 将留言列表转换为前端需要的格式
            List<Map<String, Object>> messageList = new ArrayList<>();
            if (messages != null) {
                for (Message message : messages) {
//                    System.out.println("处理留言: " + message.getId());
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("id", message.getId());
                    messageMap.put("content", message.getContent());
                    messageMap.put("username", message.getUsername());
                    messageMap.put("userphoto", message.getUserphoto());
                    messageMap.put("createtime", message.getCreatetime());
                    messageList.add(messageMap);
                }
            }
            
//            System.out.println("messageList: " + messageList);
            response.put("error_message", "success");
            response.put("messages", messageList);
        } catch (Exception e) {
            System.out.println("获取留言时发生错误: " + e.getMessage());
            e.printStackTrace();
            response.put("error_message", "获取留言失败");
        }

        return response;
    }
} 