package com.mcm.backend.controller.user.profile;

import com.mcm.backend.pojo.User;
import com.mcm.backend.service.user.profile.GetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetUserController {
    // 根据userId取得user对象
    @Autowired
    GetUserService getUserService;
    @GetMapping("/user/profile/get/")
    User getUser(@RequestParam Integer userId){
        System.out.println("userId " + userId);
        User res = getUserService.getUser(userId);
        System.out.println("返回：" + res);
        return res;
    }
}
