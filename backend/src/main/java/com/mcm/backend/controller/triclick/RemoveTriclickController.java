package com.mcm.backend.controller.triclick;

import com.mcm.backend.service.triclick.RemoveTriclickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveTriclickController {
    @Autowired
    RemoveTriclickService removeTriclickService;

    @PostMapping("/triclick/remove/")
    public String removeTriclick(@RequestParam Map<String, String> data){
        return  removeTriclickService.removeTriclick(data);
    }
}
