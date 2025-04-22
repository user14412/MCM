package com.mcm.backend.service.admin;

import java.util.Map;

public interface ExhibitManagementService {
    // 获取展品列表（支持分页、搜索、分类筛选）
    Map<String, Object> getExhibitList(Integer page, Integer size, String query, String category);
    
    // 添加展品
    Map<String, String> addExhibit(String name, String comment, String category, String primaryPhoto);
    
    // 修改展品信息
    Map<String, String> updateExhibit(Integer id, String name, String comment, String category, String primaryPhoto);
    
    // 删除展品
    Map<String, String> deleteExhibit(Integer id);
    
    // 获取展品图片列表
    Map<String, Object> getExhibitImages(Integer exhibitId);
    
    // 添加展品图片
    Map<String, String> addExhibitImage(Integer exhibitId, String imageUrl);
    
    // 删除展品图片
    Map<String, String> deleteExhibitImage(Integer imageId);
} 