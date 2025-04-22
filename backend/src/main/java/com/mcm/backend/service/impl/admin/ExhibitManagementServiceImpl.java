package com.mcm.backend.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mcm.backend.mapper.ExhibitMapper;
import com.mcm.backend.mapper.ImageMapper;
import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.pojo.Image;
import com.mcm.backend.service.admin.ExhibitManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExhibitManagementServiceImpl implements ExhibitManagementService {
    @Autowired
    private ExhibitMapper exhibitMapper;
    
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public Map<String, Object> getExhibitList(Integer page, Integer size, String query, String category) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");
        
        IPage<Exhibit> exhibitPage = new Page<>(page, size);
        QueryWrapper<Exhibit> queryWrapper = new QueryWrapper<>();
        
        if (query != null && !query.isEmpty()) {
            queryWrapper.like("name", query);
        }
        
        if (category != null && !category.isEmpty()) {
            queryWrapper.eq("category", category);
        }
        
        IPage<Exhibit> result = exhibitMapper.selectPage(exhibitPage, queryWrapper);
        resp.put("items", result.getRecords());
        resp.put("total", result.getTotal());
        
        return resp;
    }

    @Override
    public Map<String, String> addExhibit(String name, String comment, String category, String primaryPhoto) {
        Map<String, String> resp = new HashMap<>();
        
        Exhibit exhibit = new Exhibit();
        exhibit.setName(name);
        exhibit.setComment(comment);
        exhibit.setCategory(category);
        exhibit.setPrimaryPhoto(primaryPhoto);
        
        exhibitMapper.insert(exhibit);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> updateExhibit(Integer id, String name, String comment, String category, String primaryPhoto) {
        Map<String, String> resp = new HashMap<>();
        
        Exhibit exhibit = exhibitMapper.selectById(id);
        if (exhibit == null) {
            resp.put("error_message", "展品不存在");
            return resp;
        }
        
        exhibit.setName(name);
        exhibit.setComment(comment);
        exhibit.setCategory(category);
        if (primaryPhoto != null) {
            exhibit.setPrimaryPhoto(primaryPhoto);
        }
        
        exhibitMapper.updateById(exhibit);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> deleteExhibit(Integer id) {
        Map<String, String> resp = new HashMap<>();
        
        Exhibit exhibit = exhibitMapper.selectById(id);
        if (exhibit == null) {
            resp.put("error_message", "展品不存在");
            return resp;
        }
        
        // 删除关联的图片
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exhibit_id", id);
        imageMapper.delete(queryWrapper);
        
        // 删除展品
        exhibitMapper.deleteById(id);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, Object> getExhibitImages(Integer exhibitId) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error_message", "success");
        
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exhibit_id", exhibitId);
        List<Image> images = imageMapper.selectList(queryWrapper);
        
        resp.put("items", images);
        return resp;
    }

    @Override
    public Map<String, String> addExhibitImage(Integer exhibitId, String imageUrl) {
        Map<String, String> resp = new HashMap<>();
        
        Image image = new Image();
        image.setExhibitId(exhibitId);
        image.setUrl(imageUrl);
        
        imageMapper.insert(image);
        resp.put("error_message", "success");
        
        return resp;
    }

    @Override
    public Map<String, String> deleteExhibitImage(Integer imageId) {
        Map<String, String> resp = new HashMap<>();
        
        Image image = imageMapper.selectById(imageId);
        if (image == null) {
            resp.put("error_message", "图片不存在");
            return resp;
        }
        
        imageMapper.deleteById(imageId);
        resp.put("error_message", "success");
        
        return resp;
    }
} 