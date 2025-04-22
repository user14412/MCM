package com.mcm.backend.service.impl.feedback;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.FeedbackMapper;
import com.mcm.backend.pojo.Feedback;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.feedback.AddFeedbackService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddFeedbackServiceImpl implements AddFeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;
    @Override
    public String addFeedback(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String contactInfo = data.get("contact_info");
        String content = data.get("content");

        if(contactInfo.length() > 200){
            return "联系方式长度不超过200字符";
        }
        if(content == null || content.isEmpty()){
            return "意见内容不能为空";
        }

        Feedback feedback = new Feedback(null, contactInfo, content, null, user.getId(), null);
        feedbackMapper.insert(feedback);

        return "success";
    }
}
