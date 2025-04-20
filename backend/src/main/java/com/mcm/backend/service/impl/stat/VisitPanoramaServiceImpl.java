package com.mcm.backend.service.impl.stat;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mcm.backend.mapper.StatMapper;
import com.mcm.backend.pojo.Stat;
import com.mcm.backend.service.stat.VisitPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitPanoramaServiceImpl implements VisitPanoramaService {
    @Autowired
    StatMapper statMapper;

    @Override
    public String visitPanorama(String page) {
        // 创建一个 LambdaUpdateWrapper 实例
        LambdaUpdateWrapper<Stat> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Stat::getPage, page) // 设置条件：page 字段等于传入的 page 参数
                .setSql("count = count + 1"); // 设置 SQL 片段：count 字段加一

        // 执行更新操作
        int rowsAffected = statMapper.update(null, updateWrapper);

        // 返回操作结果
        return rowsAffected > 0 ? "success" : "failure";
    }
}
