package com.mcm.backend.service.impl.user.profile;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import com.mcm.backend.service.user.profile.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UpdatePasswordServiceImpl implements UpdatePasswordService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String updatePassword(String newPassword) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer userId = user.getId();

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        User updateUser = new User();
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        Integer rows = userMapper.update(updateUser, updateWrapper);

        return rows > 0 ? "success" : "fail";
    }
}