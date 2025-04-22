package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mcm.backend.mapper.*;
import com.mcm.backend.pojo.*;
import com.mcm.backend.service.admin.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ExhibitMapper exhibitMapper;
    
    @Autowired
    private ImageMapper imageMapper;
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private SurveyMapper surveyMapper;
    
    @Autowired
    private SurveyansMapper surveyansMapper;

    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 总用户数
            long totalUsers = userMapper.selectCount(null);
            result.put("totalUsers", totalUsers);
            
            // 管理员数量
            QueryWrapper<User> adminWrapper = new QueryWrapper<>();
            adminWrapper.eq("is_admin", true);
            long adminCount = userMapper.selectCount(adminWrapper);
            result.put("adminCount", adminCount);
            
            // 普通用户数量
            result.put("normalUserCount", totalUsers - adminCount);
            
            // 用户注册趋势（最近30天）
            List<Map<String, Object>> registrationTrend = getTrendData(userMapper, "createtime", "注册用户");
            result.put("registrationTrend", registrationTrend);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "获取用户统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getExhibitStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 展品总数
            long totalExhibits = exhibitMapper.selectCount(null);
            result.put("totalExhibits", totalExhibits);
            
            // 各分类展品数量
            List<Exhibit> exhibits = exhibitMapper.selectList(null);
            Map<String, Long> categoryCount = exhibits.stream()
                .collect(Collectors.groupingBy(Exhibit::getCategory, Collectors.counting()));
            result.put("categoryCount", categoryCount);
            
            // 展品图片总数
            long totalImages = imageMapper.selectCount(null);
            result.put("totalImages", totalImages);
            
            // 展品添加趋势（最近30天）
            List<Map<String, Object>> exhibitTrend = getTrendData(exhibitMapper, "createtime", "新增展品");
            result.put("exhibitTrend", exhibitTrend);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "获取展品统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getFeedbackStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 反馈总数
            long totalFeedback = feedbackMapper.selectCount(null);
            result.put("totalFeedback", totalFeedback);
            
            // 已解决反馈数量
            QueryWrapper<Feedback> resolvedWrapper = new QueryWrapper<>();
            resolvedWrapper.eq("resolved", true);
            long resolvedCount = feedbackMapper.selectCount(resolvedWrapper);
            result.put("resolvedCount", resolvedCount);
            
            // 未解决反馈数量
            result.put("unresolvedCount", totalFeedback - resolvedCount);
            
            // 反馈处理趋势（最近30天）
            List<Map<String, Object>> feedbackTrend = getTrendData(feedbackMapper, "createtime", "新增反馈");
            result.put("feedbackTrend", feedbackTrend);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "获取反馈统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getSurveyStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 问卷总数
            long totalSurveys = surveyMapper.selectCount(null);
            result.put("totalSurveys", totalSurveys);
            
            // 问卷回答总数
            long totalAnswers = surveyansMapper.selectCount(null);
            result.put("totalAnswers", totalAnswers);
            
            // 问卷参与趋势（最近30天）
            List<Map<String, Object>> surveyTrend = getTrendData(surveyansMapper, "createtime", "问卷回答");
            result.put("surveyTrend", surveyTrend);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "获取问卷统计数据失败");
        }
        
        return result;
    }

    private <T> List<Map<String, Object>> getTrendData(BaseMapper<T> mapper, String dateField, String name) {
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                String dateStr = date.format(formatter);
                
                QueryWrapper<T> wrapper = new QueryWrapper<>();
                wrapper.apply("DATE(createtime) = {0}", dateStr);
                
                long count = mapper.selectCount(wrapper);
                
                Map<String, Object> data = new HashMap<>();
                data.put("date", dateStr);
                data.put("count", count);
                data.put("name", name);
                
                trend.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询失败，至少返回空数据而不是抛出异常
            if (trend.isEmpty()) {
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    String dateStr = date.format(formatter);
                    Map<String, Object> data = new HashMap<>();
                    data.put("date", dateStr);
                    data.put("count", 0);
                    data.put("name", name);
                    trend.add(data);
                }
            }
        }
        
        return trend;
    }
} 