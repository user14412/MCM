package com.mcm.backend.service.impl.user.profile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.user.profile.GetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserServiceImpl implements GetUserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getUser(Integer userId) {
        return userMapper.selectById(userId);
    }
}
