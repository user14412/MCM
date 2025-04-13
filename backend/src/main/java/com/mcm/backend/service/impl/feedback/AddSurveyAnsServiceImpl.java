package com.mcm.backend.service.impl.feedback;

import com.mcm.backend.mapper.AddSurveyAnsService;
import com.mcm.backend.mapper.SurveyansMapper;
import com.mcm.backend.pojo.Surveyans;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddSurveyAnsServiceImpl implements AddSurveyAnsService {
    @Autowired
    SurveyansMapper surveyansMapper;
    @Override
    public String addSurveyAns(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer userId = user.getId();
        // 前端传int，后端以string接收
        Integer surveyId = Integer.parseInt(data.get("surveyId"));
        String decodedContent = data.get("decodedContent");

        Surveyans surveyans = new Surveyans(null, userId, decodedContent, null, surveyId);
//        System.out.println("插入答案：" + surveyans);
        surveyansMapper.insert(surveyans);
        return "success";
    }
}
