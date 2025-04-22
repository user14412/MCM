package com.mcm.backend.service.impl.user.account;

import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import com.mcm.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// 根据token获取用户信息
@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("is_admin", user.getIsAdmin().toString());
        map.put("is_banned", user.getIsBanned().toString());
        System.out.println("info:" + map);
        return map;
    }
}

