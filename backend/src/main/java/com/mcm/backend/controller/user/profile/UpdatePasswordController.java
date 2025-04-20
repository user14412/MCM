package com.mcm.backend.controller.user.profile;

import com.mcm.backend.service.user.profile.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatePasswordController {
    @Autowired
    private UpdatePasswordService updatePasswordService;

    @PostMapping("/user/password/update/")
    public String updatePassword(@RequestParam String newPassword) {
        return updatePasswordService.updatePassword(newPassword);
    }
}