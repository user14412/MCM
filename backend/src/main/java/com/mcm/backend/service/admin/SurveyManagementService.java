package com.mcm.backend.service.admin;

import java.util.Map;

public interface SurveyManagementService {
    // 获取问卷列表（支持分页）
    Map<String, Object> getSurveyList(Integer page, Integer size);
    
    // 获取问卷详情
    Map<String, Object> getSurvey(Integer id);
    
    // 创建问卷
    Map<String, String> createSurvey(String title, String content);
    
    // 更新问卷
    Map<String, String> updateSurvey(Integer id, String title, String content);
    
    // 删除问卷
    Map<String, String> deleteSurvey(Integer id);
    
    // 获取问卷回答列表
    Map<String, Object> getSurveyAnswers(Integer surveyId, Integer page, Integer size);
    
    // 获取问卷回答详情
    Map<String, Object> getSurveyAnswer(Integer answerId);
} 