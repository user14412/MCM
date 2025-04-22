package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcm.backend.mapper.SurveyMapper;
import com.mcm.backend.mapper.SurveyansMapper;
import com.mcm.backend.pojo.Survey;
import com.mcm.backend.pojo.Surveyans;
import com.mcm.backend.service.admin.SurveyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SurveyManagementServiceImpl implements SurveyManagementService {
    @Autowired
    private SurveyMapper surveyMapper;
    
    @Autowired
    private SurveyansMapper surveyansMapper;

    @Override
    public Map<String, Object> getSurveyList(Integer page, Integer size) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");
        
        IPage<Survey> surveyPage = new Page<>(page, size);
        IPage<Survey> result = surveyMapper.selectPage(surveyPage, null);
        
        resp.put("items", result.getRecords());
        resp.put("total", result.getTotal());
        
        return resp;
    }

    @Override
    public Map<String, Object> getSurvey(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        Survey survey = surveyMapper.selectById(id);
        
        if (survey == null) {
            resp.put("error_message", "问卷不存在");
            return resp;
        }
        
        resp.put("error_message", "success");
        resp.put("survey", survey);
        return resp;
    }

    @Override
    public Map<String, String> createSurvey(String title, String content) {
        Map<String, String> resp = new HashMap<>();
        
        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setContent(content);
        
        surveyMapper.insert(survey);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> updateSurvey(Integer id, String title, String content) {
        Map<String, String> resp = new HashMap<>();
        
        Survey survey = surveyMapper.selectById(id);
        if (survey == null) {
            resp.put("error_message", "问卷不存在");
            return resp;
        }
        
        survey.setTitle(title);
        survey.setContent(content);
        
        surveyMapper.updateById(survey);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> deleteSurvey(Integer id) {
        Map<String, String> resp = new HashMap<>();
        
        Survey survey = surveyMapper.selectById(id);
        if (survey == null) {
            resp.put("error_message", "问卷不存在");
            return resp;
        }
        
        // 删除关联的回答
        QueryWrapper<Surveyans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("survey_id", id);
        surveyansMapper.delete(queryWrapper);
        
        // 删除问卷
        surveyMapper.deleteById(id);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, Object> getSurveyAnswers(Integer surveyId, Integer page, Integer size) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");
        
        IPage<Surveyans> answerPage = new Page<>(page, size);
        QueryWrapper<Surveyans> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("survey_id", surveyId);
        
        IPage<Surveyans> result = surveyansMapper.selectPage(answerPage, queryWrapper);
        resp.put("items", result.getRecords());
        resp.put("total", result.getTotal());
        
        return resp;
    }

    @Override
    public Map<String, Object> getSurveyAnswer(Integer answerId) {
        Map<String, Object> resp = new HashMap<>();
        Surveyans answer = surveyansMapper.selectById(answerId);
        
        if (answer == null) {
            resp.put("error_message", "回答不存在");
            return resp;
        }
        
        resp.put("error_message", "success");
        resp.put("answer", answer);
        return resp;
    }
} 