package com.mcm.backend.service.impl.feedback;

import com.mcm.backend.mapper.SurveyMapper;
import com.mcm.backend.pojo.Survey;
import com.mcm.backend.service.feedback.GetSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetSurveyServiceImpl implements GetSurveyService {
    @Autowired
    SurveyMapper surveyMapper;
    @Override
    public Survey getSurvey(Integer surveyId) {
        // 通过问卷id surveyId 取得问卷
        Survey survey = surveyMapper.selectById(surveyId);
//        System.out.println("survey" + survey);
        return survey;
    }
}
