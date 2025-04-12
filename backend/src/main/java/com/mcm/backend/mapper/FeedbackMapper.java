package com.mcm.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mcm.backend.pojo.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
