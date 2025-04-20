package com.mcm.backend.service.impl.stat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mcm.backend.mapper.StatMapper;
import com.mcm.backend.pojo.Stat;
import com.mcm.backend.service.stat.CountPanoramaService;
import com.mcm.backend.service.stat.VisitPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountPanoramaServiceImpl implements CountPanoramaService {
    @Autowired
    StatMapper statMapper;

    @Override
    public Integer countPanorama() {
        LambdaQueryWrapper<Stat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stat::getPage, "panorama");
        Stat stat = statMapper.selectOne(queryWrapper);
        Integer res = stat != null ? stat.getCount() : 0;
        System.out.println("访问次数:" + res);
        return res;
    }
}
