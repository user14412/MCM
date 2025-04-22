package com.mcm.backend.service.impl.user.account;

import com.mcm.backend.mapper.UserMapper;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_massage", "用户名不能为空");
            return map;
        }
        if(password == null || confirmedPassword == null){
            map.put("error_massage", "密码不能为空");
            return map;
        }

        username = username.trim(); // 去除前导空格
        if(username.length() == 0){
            map.put("error_massage", "用户名不能为空");
            return map;
        }
        if(password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message", "密码不能为空");
        }
        if(username.length() > 100){
            map.put("error_message", "用户名长度不能大于100");
        }
        if(password.length() > 100 || confirmedPassword.length() > 100){
            map.put("error_message", "密码长度不能大于100");
        }
        if(!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }
        String encodedPassword = passwordEncoder.encode(password);
        // 默认头像
        String photo = "https://cdn.acwing.com/media/user/profile/photo/250444_lg_40706ecc9f.jpg";
        User user = new User(null, username, encodedPassword, photo, null, null, null);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
