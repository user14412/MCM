package com.mcm.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mcm.backend.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
