package com.mcm.backend.service.impl.exhibit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ExhibitMapper;
import com.mcm.backend.mapper.ImageMapper;
import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.service.exhibit.GetlistExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetlistExhibitServiceImpl implements GetlistExhibitService {
    @Autowired
    ExhibitMapper exhibitMapper;

    @Override
    public List<Exhibit> getlistExhibit() {
        // 取出当前所有藏品信息作为一个list返回给前端
        return exhibitMapper.selectList(new QueryWrapper<>());
    }
}
