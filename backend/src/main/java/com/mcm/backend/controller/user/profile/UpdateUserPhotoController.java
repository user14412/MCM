package com.mcm.backend.controller.user.profile;

import com.mcm.backend.service.user.profile.UpdateUserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateUserPhotoController {
    @Autowired
    UpdateUserPhotoService updateUserPhotoService;
    @PostMapping("/user/photo/update/")
    String updateUserPhoto(@RequestParam String imageUrl){
        return updateUserPhotoService.updateUserPhoto(imageUrl);
    }
}
