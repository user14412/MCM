package com.mcm.backend.service.impl.user.profile;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import com.mcm.backend.service.user.profile.UpdateUserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserPhotoServiceImpl implements UpdateUserPhotoService {
    @Autowired
    UserMapper userMapper;
    @Override
    public String updateUserPhoto(String imageUrl) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer userId = user.getId();

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        User updateUser = new User();
        updateUser.setPhoto(imageUrl);
        Integer rows = userMapper.update(updateUser, updateWrapper);

        return rows > 0 ? "success" : "fail";
    }
}
