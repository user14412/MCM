package com.mcm.backend.controller.user.profile;

import com.mcm.backend.service.user.profile.UpdateUsernameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateUsernameController {
    @Autowired
    UpdateUsernameService updateUsernameService;

    @PostMapping("/user/username/update/")
    String updateUsername(@RequestParam String username) {
        return updateUsernameService.updateUsername(username);
    }
}