package com.mcm.backend.controller.user.account;

import com.alibaba.fastjson2.JSONObject;
import com.mcm.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// 根据用户名和密码获取用户的token
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestBody JSONObject data){

        String username = data.getString("username");
        String password = data.getString("password");
        System.out.println(username+ " " + password);
        return loginService.getToken(username, password);
    }
}
